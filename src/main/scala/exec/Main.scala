package main.scala.exec

import main.scala.window.MainFrame
import main.scala.model.Game
import javax.swing.SwingUtilities
import main.scala.controller.GameCtr

// メインクラス
object Main extends App {

  val WIDTH = 800
  val HEIGHT = 600
  val ADJ_W = WIDTH - 16 // 端の枠部分を考慮した横幅、Panelの横幅を取得すべきだったか？

  val ctr = new GameCtr()

  val window = new MainFrame(ctr, "Invader")
  window.setSize(WIDTH, HEIGHT)
  window.setLocationRelativeTo(null)
  while (true) {	// ウィンドウを閉じるまで続く
    val game = Game(ctr)
    ctr.init(game, window.gp)
    window.setVisible(true)
    ctr.start()
  }

}