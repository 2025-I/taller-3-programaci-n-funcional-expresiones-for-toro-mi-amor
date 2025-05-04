package taller

import scala.annotation.tailrec

class ManiobraTrenes {
  // tipos de datos simplificados
  type Vagon = Any
  type Tren = List[Vagon]
  type Estado = (Tren, Tren, Tren)
  type Maniobra = List[Movimiento]

  // tipos de movimientos
  trait Movimiento
  case class Uno(n: Int) extends Movimiento
  case class Dos(n: Int) extends Movimiento

  // función para mover un tren de un carril a otro
  def aplicarMovimiento(e: Estado, m: Movimiento): Estado = {
    val (principal, uno, dos) = e
    m match {
      // mover del carril principal al carril uno y viceversa
      case Uno(0) => e
      case Uno(n) if (n > 0) =>
        val separador = principal.length - n
        val (resto, aMover) = if (separador >= 0) principal.splitAt(separador)
        else (Nil, principal)
        (for {
          carrilPrincipal <- List(resto)
          carrilUno <- List(aMover ++ uno)
        } yield (carrilPrincipal, carrilUno, dos)
          ).head
      case Uno(n) if (n < 0) =>
        val separador = n * (-1)
        val (aMover, resto) = uno.splitAt(separador)
        (for {
          carrilPrincipal <- List(principal ++ aMover)
          carrilUno <- List(resto)
        } yield (carrilPrincipal, carrilUno, dos)
          ).head

      // mover del carril principal al carril dos y viceversa
      case Dos(0) => e
      case Dos(n) if (n > 0) =>
        val separador = principal.length - n
        val (resto, aMover) = if (separador >= 0) principal.splitAt(separador)
        else (Nil, principal)
        (for {
          carrilPrincipal <- List(resto)
          carrilDos <- List(aMover ++ dos)
        } yield (carrilPrincipal, uno, carrilDos)
          ).head
      case Dos(n) if (n < 0) =>
        val separador = n * (-1)
        val (aMover, resto) = dos.splitAt(separador)
        (for {
          carrilPrincipal <- List(principal ++ aMover)
          carrilDos <- List(resto)
        } yield (carrilPrincipal, uno, carrilDos)
          ).head
    }
  }

  // función para aplicar una serie de movimientos a un estado
  def aplicarMovimientos(e:Estado, movs:Maniobra):List[Estado] = {
    @tailrec
    def movimientoAux(movs:Maniobra, listaEstados:List[Estado]):List[Estado] = movs match {
      case Nil => listaEstados
      case h :: t =>
        val nuevoEstado = for {
          estadoActual <- List(listaEstados.last)
          siguiente <- List(aplicarMovimiento(estadoActual, h))
        }
        yield siguiente
        movimientoAux(t, listaEstados :+ nuevoEstado.head)
    }
    movimientoAux(movs, List(e))
  }
  def definirManiobra(inicial:Tren, deseado:Tren):Maniobra = {
    val n = inicial.length
    val primerosPasos = List(Uno(n), Uno(-(n-1)), Dos(n-1))
    val estadoInicial = aplicarMovimientos((inicial, Nil, Nil), primerosPasos).last
    val posibleMov = List(Uno(1), Uno(-1), Dos(1), Dos(-1))

    @tailrec
    def auxiliar(pendientes:List[(Estado, Maniobra)], visitados:List[Estado]):Maniobra = pendientes match {
      case Nil => List()
      case (actual, listaMov) :: resto =>
        val (principal, uno, dos) = actual
        if (principal == deseado && uno.isEmpty && dos.isEmpty) listaMov
        else {
          val nuevoMov = for {
            mover <- posibleMov
            siguiente = aplicarMovimiento(actual, mover)
            if (siguiente != actual)
            if (!visitados.contains(siguiente))
          } yield (siguiente, listaMov :+ mover)
          auxiliar(resto ++ nuevoMov, visitados ++ nuevoMov.map(_._1)
          )
        }
    }
    auxiliar(List((estadoInicial, primerosPasos)), List(estadoInicial))
  }
}