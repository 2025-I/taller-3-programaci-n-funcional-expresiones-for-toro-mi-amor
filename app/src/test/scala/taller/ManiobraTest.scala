package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ManiobraTest extends AnyFunSuite {
    val objeto = new ManiobraTrenes()

    test("aplicarMovimientos: movimientos nulos") {
        val estado = ((1 to 10).toList, Nil, Nil)
        val movimientos = List(objeto.Uno(0), objeto.Dos(0))
        assert(estado == objeto.aplicarMovimientos(estado, List()).last)
    }

    test("aplicarMovimientos: 1 al 10") {
        val estado = ((1 to 10).toList, Nil, Nil)
        val deseado = (List(1,2,3,6,4,7), List(8,9,10), List(5))
        val movimientos = List(objeto.Uno(5), objeto.Dos(2), objeto.Uno(-2), objeto.Dos(-1), objeto.Uno(1), objeto.Dos(1), objeto.Uno(-1), objeto.Dos(-1), objeto.Uno(3), objeto.Uno(-3))
        assert(deseado == objeto.aplicarMovimientos(estado, movimientos).last)
    }
}