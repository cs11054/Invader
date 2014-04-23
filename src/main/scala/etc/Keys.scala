package main.scala.etc

// キーを管理する 、列挙
sealed abstract class Keys
object Keys {
  case object Right extends Keys
  case object Left extends Keys
  case object Space extends Keys
  case object Enter extends Keys
}