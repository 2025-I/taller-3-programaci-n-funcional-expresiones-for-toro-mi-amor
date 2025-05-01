package taller

class ManiobraTrenes {
  // tipos de datos simplificados
  type Vagon  = Any
  type Tren   = List[Vagon]
  type Estado = (Tren, Tren, Tren)
  type Maniobra = List[Movimiento]

  // tipos de movimientos
  trait Movimiento
  case class Uno(n:Int) extends Movimiento
  case class Dos(n:Int) extends Movimiento

  // función para mover un tren de un carril a otro
  def aplicarMovimiento(e:Estado, m:Movimiento):Estado = {
    val (principal, uno, dos) = e
    m match {
      // mover del carril principal al carril uno y viceversa
      case Uno(0) => e
      case Uno(n) if (n>0) =>
        val separador = principal.length - n
        val (resto, aMover) = if (separador >= 0) principal.splitAt(separador)
                              else (Nil, principal)
        (for {
          carrilPrincipal <- List(resto)
          carrilUno       <- List(aMover ++ uno)
          } yield (carrilPrincipal, carrilUno, dos)
        ).head
      case Uno(n) if (n<0) =>
        val k = n*(-1)
        val (aMover, resto) = uno.splitAt(k)
        (for {
          carrilPrincipal <- List(principal ++ aMover)
          carrilUno       <- List(resto)
          } yield (carrilPrincipal, carrilUno, dos)
        ).head

      // mover del carril principal al carril dos y viceversa
      case Dos(0) => e
      case Dos(n) if (n>0) =>
        val separador = principal.length - n
        val (resto, aMover) = if (separador >= 0) principal.splitAt(separador)
                              else (Nil, principal)
        (for {
          carrilPrincipal <- List(resto)
          carrilDos <- List(aMover ++ dos)
          } yield (carrilPrincipal, uno, carrilDos)
        ).head
      case Dos(n) if (n<0) =>
        val separador = n*(-1)
        val (aMover, resto) = dos.splitAt(separador)
        (for {
          carrilPrincipal <- List(principal ++ aMover)
          carrilDos       <- List(resto)
          } yield (carrilPrincipal, uno, carrilDos)
        ).head
    }
  }
}