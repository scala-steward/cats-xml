package cats.xml.codec

import cats.Eq
import cats.laws.discipline.ContravariantTests
import cats.xml.{Xml, XmlData, XmlNode}
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbInt
import org.scalacheck.Prop.forAll

class EncoderSuite extends munit.FunSuite {

  test("Encoder.of") {
    assertEquals(
      Encoder.of[Int](x => XmlNode(x.toString)).encode(1),
      XmlNode("1")
    )
  }

  test("Encoder.pure") {
    assertEquals(
      Encoder.pure(XmlNode("Foo")).encode(1),
      XmlNode("Foo")
    )
  }

  test("Encoder.id") {
    assertEquals(
      Encoder.id.encode(XmlNode("Foo")),
      XmlNode("Foo")
    )
  }

  test("Encoder.apply") {
    assertEquals(
      Encoder[Int].encode(1),
      XmlData.fromInt(1)
    )
  }

}

class EncoderInstancesSuite extends munit.ScalaCheckSuite {

  import cats.xml.testing.arbitrary.CommonArbitrary.*
  import cats.xml.testing.arbitrary.XmlDataArbitrary.*

  property(s"Encoder.encodeOption") {
    forAll { (value: Option[Int]) =>
      assertEquals(
        obtained = Encoder.encodeOption[Int].encode(value),
        expected = value.fold(Xml.Null)(XmlData.fromInt)
      )
    }
  }

  property(s"Encoder.encodeSomeOption") {
    forAll { (value: Some[Int]) =>
      assertEquals(
        obtained = Encoder.encodeSomeOption[Int].encode(value),
        expected = XmlData.fromInt(value.value)
      )
    }
  }

  test("Encoder.encodeNoneOption") {
    assertEquals(
      obtained = Encoder.encodeNoneOption.encode(None),
      expected = Xml.Null
    )
  }

  property(s"Encoder.dataEncodeOption") {
    forAll { (value: Option[Int]) =>
      assertEquals(
        obtained = Encoder.dataEncodeOption[Int].encode(value),
        expected = value.fold(Xml.Null)(XmlData.fromInt)
      )
    }
  }

  property(s"Encoder.dataEncodeSomeOption") {
    forAll { (value: Some[Int]) =>
      assertEquals(
        obtained = Encoder.dataEncodeSomeOption[Int].encode(value),
        expected = XmlData.fromInt(value.value)
      )
    }
  }

  test("DataEncoder.dataEncodeNoneOption") {
    assertEquals(
      obtained = Encoder.dataEncodeNoneOption.encode(None),
      expected = Xml.Null
    )
  }

  test("Encoder.encodeUnit") {
    assertEquals(
      obtained = Encoder.encodeUnit.encode(()),
      expected = Xml.Null
    )
  }

  property(s"Encoder.encoderXmlData") {
    forAll { (value: XmlData) =>
      assertEquals(
        obtained = Encoder.encodeXmlData.encode(value),
        expected = value
      )
    }
  }

  property(s"Encoder.encodeString") {
    forAll { (value: String) =>
      assertEquals(
        obtained = Encoder.encodeString.encode(value),
        expected = XmlData.fromString(value)
      )
    }
  }

  property(s"Encoder.encodeChar") {
    forAll { (value: Char) =>
      assertEquals(
        obtained = Encoder.encodeChar.encode(value),
        expected = XmlData.fromChar(value)
      )
    }
  }

  property(s"Encoder.encodeBoolean") {
    forAll { (value: Boolean) =>
      assertEquals(
        obtained = Encoder.encodeBoolean.encode(value),
        expected = XmlData.fromBoolean(value)
      )
    }
  }

  property(s"Encoder.encodeInt") {
    forAll { (value: Int) =>
      assertEquals(
        obtained = Encoder.encodeInt.encode(value),
        expected = XmlData.fromInt(value)
      )
    }
  }

  property(s"Encoder.encodeLong") {
    forAll { (value: Long) =>
      assertEquals(
        obtained = Encoder.encodeLong.encode(value),
        expected = XmlData.fromLong(value)
      )
    }
  }

  property(s"Encoder.encodeFloat") {
    forAll { (value: Float) =>
      assertEquals(
        obtained = Encoder.encodeFloat.encode(value),
        expected = XmlData.fromFloat(value)
      )
    }
  }

  property(s"Encoder.encodeDouble") {
    forAll { (value: Double) =>
      assertEquals(
        obtained = Encoder.encodeDouble.encode(value),
        expected = XmlData.fromDouble(value)
      )
    }
  }

  property(s"Encoder.encodeBigDecimal") {
    forAll { (value: BigDecimal) =>
      assertEquals(
        obtained = Encoder.encodeBigDecimal.encode(value),
        expected = XmlData.fromBigDecimal(value)
      )
    }
  }

  property(s"Encoder.encodeBigInt") {
    forAll { (value: BigInt) =>
      assertEquals(
        obtained = Encoder.encodeBigInt.encode(value),
        expected = XmlData.fromBigInt(value)
      )
    }
  }
}

class EncoderCatsInstancesSuite extends munit.DisciplineSuite {

  import cats.syntax.all.*
  import cats.xml.testing.arbitrary.CodecArbitrary.*

  implicit def eqEncoder[T: Arbitrary: Eq]: Eq[Encoder[T]] =
    (x: Encoder[T], y: Encoder[T]) => {
      val value: T = implicitly[Arbitrary[T]].arbitrary.sample.get
      x.encode(value).eqv(y.encode(value))
    }

  checkAll(
    "Encoder.ContravariantTests",
    ContravariantTests[Encoder].contravariant[Int, Int, String]
  )
}
