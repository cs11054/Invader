package main.scala.etc

// シーンを管理する、列挙
sealed abstract class Scene
object Scene {
  case object MENU extends Scene
  case object GAME extends Scene
  case object FINISH extends Scene
}