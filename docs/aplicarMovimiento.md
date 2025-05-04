# aplicarMovimiento

Antes de explicar la función, hay que ponernos en contexto sobre los
diferentes tipos de valores con los que trabaja el sistema. Estos son:
- `Tren:` el cual representa una lista de **vagones.**
- 
$$
T = List[V]
$$

- `Estado:` el cual representa un conjunto de tres **trenes.** Cada
posición contiene el tren ubicado en los carriles: $P$rincipal, $U$no
y $D$os respectivamente.

$$
E = (P,U,D)
$$

Finalmente, tenemos los tipos de **movimientos,** siendo las acciones
que definen a qué carril se moverá el tren. Estos son:
- $Uno(n)$: donde $n$ es el número de vagones que se moverán del carril
`principal` al carril `uno` y viceversa.
- $Dos(n)$: donde $n$ es el número de vagones que se moverán del carril
    `principal` al carril `dos` y viceversa.

Nótese que en ambos casos hay un **viceversa,** esto debido a que cuando
el valor $n>0$, el tren se moverá hacia adelante, ubicándose en el carril
`uno` o `dos` respectivamente; pero cuando el valor $n<0$, el tren se
devolverá del carril en el que se encuentre al principal.

Dicho esto, podemos pasar a explicar el código:
    
    def aplicarMovimiento(e: Estado, m: Movimiento): Estado = {
        val (principal, uno, dos) = e
        m match {
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

Vamos por partes, la función recibe dos parámetros: un conjunto `e` de
tipo $E$ y una acción `m` de tipo $Movimiento$, que podemos denotar
simplemente como $M$. Este valor `m` se compara para conocer el tipo
de movimiento que corresponde y en base a esto continúa con el proceso
para mover o devolver el tren dependiendo del caso. Debido a que ambas
partes trabajan de forma análoga, se va a explicar solamente el
caso $Uno(n)$.

Al tener el movimiento $Uno(n)$ se hacen tres verificaciones:
- `n = 0:` al ser un movimiento nulo no ocurre nada y se devuelve el
estado `e` sin modificar.

- `n > 0:` como habíamos explicado anteriormente, cuando el valor $n$
es positivo ese mismo número de vagones ubicados en el tren principal
pasan al carril `uno`. Para esto, el programa define un separador
restando ese valor $n$ a la cantidad total de vagones del tren y en
base a dicho valor define dos listas: `aMover` y `resto`. Hecho esto,
devuelve el estado `e` modificado, actualizando el carril $P$ con la
lista `resto` y uniendo los vagones del carril $U$ con los de la lista
`aMover`, posicionándolos a la izquierda.

- `n < 0:` funciona de forma similar al caso anterior, la diferencia es
que ahora el programa define el separador multiplicando el valor $n$
por `-1`. Además el estado `e` modificado será devuelto con respecto
el carril $P$, es decir que los vagones del carril $P$ se unirán con
los de la lista `aMover` siendo ubicados a la derecha y el carril $U$
será actualizado con la lista `resto`.

Podemos expresar estas verificaciones de la siguiente forma:

$$
f(e,n) =
\begin{cases}
e, & \text{si } n = 0 \\
e = (P_{resto} \text{, } P_{aMover} + U \text{, } D), & \text{si } n > 0 \\
e = (P + U_{aMover} \text{, } U_{resto} \text{, } D), & \text{si } n < 0
\end{cases}
$$

$$
g(e,n) =
\begin{cases}
e, & \text{si } n = 0 \\
e = (P_{resto} \text{, } U \text{, } P_{aMover} + D), & \text{si } n > 0 \\
e = (P + D_{aMover} \text{, } U \text{, } D_{resto}), & \text{si } n < 0
\end{cases}
$$

Y así mismo, la función aplicarMovimiento podemos expresarla de la
siguiente forma:

$$
aplicarMovimiento(e, m) =
\begin{cases}
f(e,n), & \text{si } m = Uno(n) \\
g(e,n), & \text{si } m = Dos(n) \\
\end{cases}
$$

# Ejemplo
Ya conociendo todo el funcionamiento del código pasemos a un pequeño
ejemplo para observar su comportamiento,supongamos que tenemos la
siguiente lista de vagones:

$$
List(1,2,3,4,5,6)
$$

Como podemos ver es un tren de tamaño 6 y queremos hacer algo sencillo:
mover el vagón `6` de forma que quede de primero. Para esto se podrían
emplear los siguientes movimientos:

$$
Uno(6) \text{ - } Uno(-5) \text{ - } Dos(5) \text{ - }  Uno(-1) \text{ - } Dos(-5)
$$

Lo que paso por paso se vería de la siguiente manera:
    
    (List(1,2,3,4,5,6), Nil, Nil) // estado inicial
    (Nil, List(1,2,3,4,5,6), Nil) // Uno(6), se mueven todos los vagones al carril Uno
    (List(1,2,3,4,5), List(6), Nil) // Uno(-5), se devuelven todos los vagones (expecto el 6) al carril Principal
    (Nil, List(6), List(1,2,3,4,5)) // Dos(5), se mueven todos los vagones del carril Principal al carril Dos
    (List(6), Nil, List(1,2,3,4,5)) // Uno(-1), se devuelve el vagon 6 al carril Principal
    (List(6,1,2,3,4,5), Nil, Nil) // Dos(-5), se devuelven todos los vagones del carril Dos al carril Principal