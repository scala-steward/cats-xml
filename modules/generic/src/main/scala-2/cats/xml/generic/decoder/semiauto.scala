package cats.xml.generic.decoder

import cats.xml.codec.Decoder
import magnolia1.*

import scala.reflect.ClassTag
import scala.reflect.runtime.universe.*

object semiauto {

  type Typeclass[T] = Decoder[T]

  def deriveDecoder[T]: Decoder[T] =
    macro Magnolia.gen[T]

  def join[T: TypeTag: ClassTag](ctx: CaseClass[Decoder, T]): Decoder[T] =
    DecoderDerivation.join(ctx)
}
