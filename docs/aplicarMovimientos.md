# aplicarMovimientos

Como pudimos ver en la función [`aplicarMovimiento`](docs/aplicarMovimiento.md),
esta se encargaba de aplicar un movimiento al estado que le proporcionaramos,
pero lo hacía de forma unitaria, por lo que si queríamos aplicar varios
movimientos a un mismo estado teníamos que hacer varios llamados y a su vez
recordar el estado anterior.

Para facilitar un poco más esta tarea se creó la siguiente función:

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

Como podemos ver, la función es muy similar a su antecesora recibiendo
incluso parámetros similares, pero con una pequeña diferencia: en lugar de
recibir un solo movimiento por llamado ahora recibe una lista de movimientos
a la cual llamaremos `Maniobra` que podemos expresar de la siguiente manera:

$$
Ma = List[M]
$$

Esta función internamente contiene una función auxiliar llamada `movimientoAux`,
la cual recibe dos listas: la primera llamada `movs` de tipo $Ma$ y la segunda
`listaEstados` que como su nombre lo dice, irá almacenando los distintos que
tenga el estado inicial al aplicar cada movimiento en orden. Esta función se
encargará de aplicar recursivamente cada movimiento de la lista `movs` siempre
y cuando la lista no se encuentre vacía, cuando esto pase será retornada la
lista de estados obtenida tras aplicar todos los movimientos.

Sabiendo esto, podemos expresar la función de la siguiente manera:


Podemos expresar estas verificaciones de la siguiente forma:

$$
movimientoAux(movs,le) =
\begin{cases}
le, & \text{si } movs = \emptyset \\
movimientoAux(movs - M_{actual} \text{, } le + E_{actual}), & \text{si } movs \neq \emptyset \\
\end{cases}
$$


# Primer ejemplo
Ya conociendo todo el funcionamiento del código pasemos a observar su
comportamiento con el mismo ejemplo de la función anterior:

$$
(List(1,2,3,4,5,6),Nil,Nil)
$$

Como podemos recordar, queríamos que el vagón `6` quedara de primero
y para eso aplicamos la misma serie de movimientos que ahora
almacenaremos en una lista:

$$
List(Uno(6),Uno(-5),Dos(5),Uno(-1),Dos(-5))
$$

Al llamar a la función pasándole ambas listas, obtendremos ahora la
lista de cambios por las que pasó el estado en el transcurso de la
ejecución:

    List
    (
        (List(1,2,3,4,5,6), Nil, Nil) // estado inicial
        (Nil, List(1,2,3,4,5,6), Nil)   // Uno(6), se mueven todos los vagones al carril Uno
        (List(1,2,3,4,5), List(6), Nil) // Uno(-5), se devuelven todos los vagones (expecto el 6) al carril Principal
        (Nil, List(6), List(1,2,3,4,5)) // Dos(5), se mueven todos los vagones del carril Principal al carril Dos
        (List(6), Nil, List(1,2,3,4,5)) // Uno(-1), se devuelve el vagon 6 al carril Principal
        (List(6,1,2,3,4,5), Nil, Nil)   // Dos(-5), se devuelven todos los vagones del carril Dos al carril Principal
    )

# Segundo ejemplo
Ahora pensemos en un ejemplo un poco más extenso, supongamos que
tenemos el siguiente estado con una lista de vagones de tamaño 10:

$$
(List(1,2,3,4,5,6,7,8,9,10),Nil,Nil)
$$

Y queremos aplicarle una serie de movimientos más grande, en este
caso la siguiente lista de 10 movimientos aleatorios:

$$
List(Uno(5), Dos(2), Uno(-2), Dos(-1), Uno(1), Dos(1), Uno(-1), Dos(-1), Uno(3), Uno(-3))
$$

Al llamar a la función, daría como resultado la siguiente lista
de cambios por los que pasó nuestro estado inicial:

    List(
        (List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), List(), List()),
        (List(1, 2, 3, 4, 5), List(6, 7, 8, 9, 10), List()),
        (List(1, 2, 3), List(6, 7, 8, 9, 10), List(4, 5)),
        (List(1, 2, 3, 6, 7), List(8, 9, 10), List(4, 5)),
        (List(1, 2, 3, 6, 7, 4), List(8, 9, 10), List(5)),
        (List(1, 2, 3, 6, 7), List(4, 8, 9, 10), List(5)),
        (List(1, 2, 3, 6), List(4, 8, 9, 10), List(7, 5)),
        (List(1, 2, 3, 6, 4), List(8, 9, 10), List(7, 5)),
        (List(1, 2, 3, 6, 4, 7), List(8, 9, 10), List(5)),
        (List(1, 2, 3), List(6, 4, 7, 8, 9, 10), List(5)),
        (List(1, 2, 3, 6, 4, 7), List(8, 9, 10), List(5))
    )
