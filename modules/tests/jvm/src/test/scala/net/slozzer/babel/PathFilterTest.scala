package net.slozzer.babel

import munit.FunSuite
import java.nio.file.{Path => JPath}

final class PathFilterTest extends FunSuite {
  test("extension") {
    assertEquals(obtained = PathFilter.extension("conf").matches(JPath.of("foo.conf")), expected = true)
    assertEquals(obtained = PathFilter.extension("conf").matches(JPath.of("foo/bar.conf")), expected = true)
    assertEquals(obtained = PathFilter.extension("conf").matches(JPath.of("foo.json")), expected = false)
    assertEquals(obtained = PathFilter.extension("conf").matches(JPath.of("foo")), expected = false)
  }

  test("parent") {
    assertEquals(obtained = PathFilter.parent("foo").matches(JPath.of("foo/bar.txt")), expected = true)
    assertEquals(obtained = PathFilter.parent("foo").matches(JPath.of("bar.txt")), expected = false)
    assertEquals(obtained = PathFilter.parent("foo").matches(JPath.of("foo")), expected = false)
  }

  test("&&") {
    val filter = PathFilter.parent("foo") && PathFilter.extension("conf")
    assertEquals(obtained = filter.matches(JPath.of("foo/bar.conf")), expected = true)
    assertEquals(obtained = filter.matches(JPath.of("foo/bar.json")), expected = false)
    assertEquals(obtained = filter.matches(JPath.of("bar.conf")), expected = false)
    assertEquals(obtained = filter.matches(JPath.of("bar.json")), expected = false)
  }

  test("||") {
    val filter = PathFilter.parent("foo") || PathFilter.extension("conf")
    assertEquals(obtained = filter.matches(JPath.of("foo/bar.conf")), expected = true)
    assertEquals(obtained = filter.matches(JPath.of("foo/bar.json")), expected = true)
    assertEquals(obtained = filter.matches(JPath.of("bar.conf")), expected = true)
    assertEquals(obtained = filter.matches(JPath.of("bar.json")), expected = false)
  }
}
