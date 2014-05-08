package main.scala.character
import javax.imageio.ImageIO
import java.io.File
import main.scala.exec.Main
import scala.Array.canBuildFrom
import scala.swing.Dialog
import scala.swing.Dialog.Message

// キャラクターたちの親、Objectって名前でもいいかもしれないけど紛らわしいので却下
abstract class Character(var x: Int, var y: Int, name: String) {
  var life = true
  val icon = Character.imgMap.get(name) match {
    case None =>
      Dialog.showMessage(message = s"${Character.PATH}に${name}.pngを配置してください。", title = "必要な画像がないよ。", messageType = Message.Error)
      throw new IllegalArgumentException("その画像は存在しません。")
    case Some(x) => x
  }
  val w = icon.getWidth()
  val h = icon.getHeight()
  val bullets: Seq[RectBullet]

  def hit() { life = false }

  def shot() {
    bullets.find(!_.enable).foreach { b =>
      b.x = x + (w - b.w) / 2
      b.y = y + h / 2
      b.enable = true
    }
  }

}

object Character {
  val EXT = ".png"
  val PATH = "./res/"
  val imgs = Option((new File(PATH)).list()) match {
    case Some(list) => list.filter(_.endsWith(EXT))
    case None =>
      Dialog.showMessage(message = s"${PATH}に必要な画像を配置してください。", title = "必要な画像がないよ。", messageType = Message.Error)
      throw new IllegalArgumentException("必要な画像がありません。")
  }
  val imgMap = (for (img <- imgs) yield (img.dropRight(EXT.length), ImageIO.read(new File(PATH + img)))) toMap
}
