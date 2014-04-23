package main.scala.character

// 弾の親、弾が持ってそうなものを持ってる
abstract class AbstractBullet {
  var x: Int = _
  var y: Int = _
  var enable: Boolean = false // その弾が有効化どうか

  def move():Unit
  def isHit(trg:Character):Boolean
   
}