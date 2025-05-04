package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import scala.util.Random

@RunWith(classOf[JUnitRunner])
class ManiobraTest extends AnyFunSuite {
    val objeto = new ManiobraTrenes()

    test("aplicarMovimientos: nule movements") {
        val estado = ((1 to 10).toList, Nil, Nil)
        val movimientos = List(objeto.Uno(0), objeto.Dos(0))
        assert(estado == objeto.aplicarMovimientos(estado, List()).last)
    }

    test("aplicarMovimientos: 1 to 10") {
        val estado = ((1 to 10).toList, Nil, Nil)
        val deseado = (List(1,2,3,6,4,7), List(8,9,10), List(5))
        val movimientos = List(objeto.Uno(5), objeto.Dos(2), objeto.Uno(-2), objeto.Dos(-1), objeto.Uno(1), objeto.Dos(1), objeto.Uno(-1), objeto.Dos(-1), objeto.Uno(3), objeto.Uno(-3))
        assert(deseado == objeto.aplicarMovimientos(estado, movimientos).last)
    }
    test("aplicarMovimientos: 1 to 50 by 5") {
        val estado = ((1 to 50 by 5).toList, Nil, Nil)
        val deseado = (List(1, 6, 11, 26, 16, 31), List(36, 41, 46), List(21))
        val movimientos = List(objeto.Uno(5),objeto.Dos(2),objeto.Uno(-2),objeto.Dos(-1),objeto.Uno(1),objeto.Dos(1),objeto.Uno(-1),objeto.Dos(-1),objeto.Uno(3),objeto.Uno(-3))
        assert(deseado == objeto.aplicarMovimientos(estado, movimientos).last)
    }

    test("aplicarMovimientos: -30 to -3 by 3") {
        val estado = ((-30 to -1 by 3).toList, Nil, Nil)
        val deseado =  (List(-30, -27, -24, -21, -12, -9), List(-18, -15), List(-6, -3))
        val movimientos = List(objeto.Dos(4), objeto.Uno(3), objeto.Uno(-1), objeto.Dos(-2), objeto.Uno(2), objeto.Uno(-2), objeto.Dos(1), objeto.Dos(-1), objeto.Uno(1), objeto.Uno(-1))
        assert(deseado == objeto.aplicarMovimientos(estado, movimientos).last)
    }

    test("definirManiobra: PDF example") {
        val inicial = List(1,2,3,4)
        val deseado = List(4,2,3,1)
        val maniobra = objeto.definirManiobra(inicial, deseado)
        val obtenido = objeto.aplicarMovimientos((inicial,Nil,Nil), maniobra).last
        assert( (deseado,Nil,Nil) == obtenido)
    }

    test("definirManiobra: 1 to 6") {
        val inicial = List(1,2,3,4,5,6)
        val deseado = List(4,5,2,6,3,1)
        val maniobra = objeto.definirManiobra(inicial, deseado)
        val obtenido = objeto.aplicarMovimientos((inicial,Nil,Nil), maniobra).last
        assert( (deseado,Nil,Nil) == obtenido)
    }

    test("definirManiobra: 1 to 6 shuffled inverted") {
        val inicial = List(6,5,4,3,2,1)
        val deseado = Random.shuffle(inicial)
        val maniobra = objeto.definirManiobra(inicial, deseado)
        val obtenido = objeto.aplicarMovimientos((inicial,Nil,Nil), maniobra).last
        assert( (deseado,Nil,Nil) == obtenido)
    }

    test("definirManiobra: shuffled vocals") {
        val inicial = List('a','e','i','o','u')
        val deseado = Random.shuffle(inicial)
        val maniobra = objeto.definirManiobra(inicial, deseado)
        val obtenido = objeto.aplicarMovimientos((inicial,Nil,Nil), maniobra).last
        assert( (deseado,Nil,Nil) == obtenido)
    }
}