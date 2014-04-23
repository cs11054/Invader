package main.scala.controller

import main.scala.model.Game
import javax.swing.JPanel
import java.awt.event.KeyEvent
import main.scala.etc.Keys
import main.scala.etc.Scene
import main.scala.character._

// ゲームのController
class GameCtr {
  var game: Game = _ // Model
  var gp: JPanel = _ // View
  var scene: Scene = Scene.MENU // 今どのシーン(Menu,Game,...)か表す
  val keys = scala.collection.mutable.Set.empty[Keys] // 押されているキー

  // Viewが描画するときに必要なオブジェクトを渡す 
  def getObj(): (Player, Seq[Enemy], Seq[Wall]) = (game.player, game.enemys, game.walls)

  // Modelから呼ばれる
  def paint() = gp.repaint()

  // キーが押されたら登録
  def keyPressed(e: KeyEvent) = e.getKeyCode() match {
    case KeyEvent.VK_RIGHT => keys += (Keys.Right)
    case KeyEvent.VK_LEFT => keys += (Keys.Left)
    case KeyEvent.VK_SPACE => keys += (Keys.Space)
    case KeyEvent.VK_ENTER => keys += (Keys.Enter)
    case _ =>
  }
  
  // キーが離されたら登録
  def keyReleased(e: KeyEvent) = e.getKeyCode() match {
    case KeyEvent.VK_RIGHT => keys -= (Keys.Right)
    case KeyEvent.VK_LEFT => keys -= (Keys.Left)
    case KeyEvent.VK_SPACE => keys -= (Keys.Space)
    case KeyEvent.VK_ENTER => keys -= (Keys.Enter)
    case _ =>
  }

  // ゲームを開始する
  def start() = game.start()

  // ゲームをクリアした時間、もっといい変数名がほしい
  def clearTime = (game.endTime - game.startTime) / 1000

  // 初期化
  def init(ga: Game, pa: JPanel) {
    game = ga
    gp = pa
    scene = Scene.MENU
  }

}