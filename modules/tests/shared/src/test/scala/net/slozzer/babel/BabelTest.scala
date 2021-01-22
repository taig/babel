//package net.slozzer.babel
//
//import net.slozzer.babel.Locales
//import munit.FunSuite
//
//final class BabelTest extends FunSuite {
//  test("merge") {
//    val left = Babel(Segments.one("x", Translation.of("fallback1")(Locales.de -> Quantities.one("foo"))))
//    val right = Babel(Segments.one("x", Translation.of("fallback2")(Locales.en -> Quantities.one("bar"))))
//    val babel = (left merge right).toOption.get
//    assertEquals(obtained = babel(Path.one("x"), Locales.de), expected = "foo")
//    assertEquals(obtained = babel(Path.one("x"), Locales.en), expected = "bar")
//    assertEquals(obtained = babel(Path.one("x"), Locales.es), expected = "fallback2")
//  }
//}
