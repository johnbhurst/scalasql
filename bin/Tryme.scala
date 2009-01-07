


def f(x: Int): Int = {
  println("f(" + x + ")")
  x
}


val a = new Array[Int](3)
for (i <- 0 until 3) {
  a(i) = f(i)
}
//for (i <- 0 until 3)
//  yield f(i)


println("computed.")
for (v <- a) {
  println(v)
}


