# definirManiobra

Finalmente, pasamos a la última función del sistema, la cual ahora
no se encargará de solo aplicar movimientos si no que tendrá la tarea
de encontrar la serie de movimientos que se necesita para lograr que
el orden de los vagones de un tren `inicial` pase a ser igual que los de
un tren `deseado`. La función ha sido planteada de la siguiente manera: 

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

Como podemos ver, al recibir ambos trenes la función se encarga de calcular
la cantidad de vagones `n` que tiene el tren `inicial`, luego haciendo uso
de este valor define una serie de tres primeros pasos: lleva todos los
vagones al carril `uno`, devuelve cantidad de `n-1` vagones **(es decir, todos
los vagones menos el primero ubicado a la derecha)** y finalmente esa cantidad
de `n-1` vagones la inserta en el carril `dos`. Esto da como resultado que
un solo vagón quede ubicado en el primer carril y en el segundo quede el resto.

Después de esto los movimientos estarán limitados a solamente cuatro
con el fin de no expandir demasiado las posibilidades y facilitar el
cálculo de los caminos:

$$
Uno(1), Uno(-1),
Dos(1), Dos(-1)
$$

Continuando, el programa se encarga de llamar a su función `auxiliar`, que recibe
dos listas: una lista de `pendientes` que almacena un conjunto de los vagones
pendientes por ser movidos junto con la lista de maniobras que se han realizado
hasta el momento; el segundo parámetro es una lista de `visitados` que lleva el
registro del cambio que han tenido los estados hasta el momento, de forma similar
a como lo hacía la función `aplicarMovimientos`.

Esta función auxiliar se encarga de obtener las posibles series
de movimientos que se le pueden aplicar al estado `actual` y así
verificar cuales cumplen con la condición de que el estado
`siguiente` sea diferente al `actual` **(esto con el fin de evitar
movimientos nulos)** y que este estado sea diferente a los ya
explorados para así redundar en caminos y ubicaciones en las que
ya se ha indagado.

Cuando todo se cumple de forma exitosa, se llama recursivamente a
la función auxiliar, agregando el nuevo conjunto a la lista de
pendientes y agregando el estado obtenido a la lista de `visitados`.
Podemos expresar esta verificación de la siguiente forma:

$$
f(p,l_{mov}) =
\begin{cases}
l_{mov}, & \text{si } p = \emptyset \\
auxiliar(p_{nuevo} + p \text{, } l_{mov} + E_{actual}), & \text{si } p \neq \emptyset \\
\end{cases}
$$

Con toda esta información, las funciones quedarían
formuladas de la siguiente manera:

$$
auxiliar(p,l_{mov}) =
\begin{cases}
List(), & \text{si } p = \emptyset \\
f(p,l_{mov}), & \text{si } p \neq \emptyset \\
\end{cases}
$$

$$
definirManiobra(t_{inicial},t_{deseado}) = auxiliar(List((E_{inicial}, pasos)) \text{, } List(E_{inicial}))
$$


# Primer ejemplo
Ya conociendo todo el funcionamiento del código pasemos a observar su
comportamiento con el siguiente ejemplo:

$$
t_{inicial} = List(1,2,3,4,5,6)
$$

$$
t_{deseado} = List(4,5,2,6,3,1)
$$

Queremos llegar del tren `inicial` al tren `deseado`, pero no sabemos
cómo. Para eso, llamamos a la función asignando ambos trenes para que
esta nos ayude a encontrar la serie de movimientos que nos ayudarán
a obtener el tren deseado:

    definirManiobra(List(1,2,3,4,5,6), List(4,5,2,6,3,1))

Al ejecutar la función, esta nos entregará la siguiente serie de
movimientos:

    List(
        Uno(6), 
        Uno(-5), 
        Dos(5), 
        Uno(-1), 
        Dos(-1), 
        Uno(1), 
        Uno(1), 
        Dos(-1), 
        Uno(1), 
        Dos(-1),
        Uno(1), 
        Dos(-1), 
        Dos(-1), 
        Uno(-1), 
        Dos(1), 
        Uno(-1), 
        Uno(-1), 
        Dos(-1), 
        Uno(-1)
    )

Pero de primeras no tenemos forma de saber si realmente esa
serie de movimientos logra dar con nuestro tren deseado, por
lo tanto llamamos a la función  `aplicarMovimientos` para que
esta los aplique y así verificar que el resultado es correcto.

    aplicarMovimientos( (inicial,Nil,Nil), maniobra)

Al hacerlo, la función nos arroja la siguiente lista de estados:

    List(
        (List(1, 2, 3, 4, 5, 6),List(),List()), 
        (List(),List(1, 2, 3, 4, 5, 6),List()), 
        (List(1, 2, 3, 4, 5),List(6),List()),
        (List(),List(6),List(1, 2, 3, 4, 5)),
        (List(6),List(),List(1, 2, 3, 4, 5)),
        (List(6, 1),List(),List(2, 3, 4, 5)),
        (List(6),List(1),List(2, 3, 4, 5)), 
        (List(),List(6, 1),List(2, 3, 4, 5)),
        (List(2),List(6, 1),List(3, 4, 5)),
        (List(),List(2, 6, 1),List(3, 4, 5)),
        (List(3),List(2, 6, 1),List(4, 5)),
        (List(),List(3, 2, 6, 1),List(4, 5)),
        (List(4),List(3, 2, 6, 1),List(5)), 
        (List(4, 5),List(3, 2, 6, 1),List()),
        (List(4, 5, 3),List(2, 6, 1),List()),
        (List(4, 5),List(2, 6, 1),List(3)),
        (List(4, 5, 2),List(6, 1),List(3)),
        (List(4, 5, 2, 6),List(1),List(3)), 
        (List(4, 5, 2, 6, 3),List(1),List()), 
        (List(4, 5, 2, 6, 3, 1),List(),List())
    )

De la lista tan inmensa que nos arroja la función,
el único resultado importante es el `último` obtenido y si
nos fijamos hemos obtenido el siguiente estado:

    (List(4, 5, 2, 6, 3, 1),List(),List())

El cual concuerda con nuestro tren deseado, por lo tanto
la serie de pasos que nos arrojó el programa ha funcionado
correctamente

> [!CAUTION]
> - Nota: la función no es muy eficiente, por lo que ante
> trenes con un tamaño superior a 6 vagones el programa
> se queda ejecutándose sin dar respuesta.