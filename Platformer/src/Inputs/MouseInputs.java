package Inputs;

import Main.GamePanel;
import gamestates.Gamestate;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInputs implements MouseListener, MouseMotionListener {
    private GamePanel gamePanel;

    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        switch (Gamestate.state) {
            case PLAYING -> {
                gamePanel.getGame().getPlaying().mouseDragged(e);
            }
            case OPTIONS -> gamePanel.getGame().getGameOptions().mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (Gamestate.state) {

            case PLAYING -> {
                gamePanel.getGame().getPlaying().mouseMoved(e);
            }
            case MENU -> {
                gamePanel.getGame().getMenu().mouseMoved(e);
            }
            case OPTIONS -> gamePanel.getGame().getGameOptions().mouseMoved(e);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //commented to test, seems to work ok
//        switch (Gamestate.state) {
//            case PLAYING -> {
//                gamePanel.getGame().getPlaying().mouseClicked(e);
//                System.out.println("Here Clicked");
//            }
//        }
   }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (Gamestate.state) {

            case PLAYING -> {
                gamePanel.getGame().getPlaying().mousePressed(e);
                System.out.println("->mouse Pressed<-");
            }
            case MENU -> {
                gamePanel.getGame().getMenu().mousePressed(e);
            }
            case OPTIONS -> gamePanel.getGame().getGameOptions().mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (Gamestate.state) {

            case PLAYING -> {
                gamePanel.getGame().getPlaying().mouseReleased(e);
            }
            case MENU -> {
                gamePanel.getGame().getMenu().mouseReleased(e);
            }
            case OPTIONS -> gamePanel.getGame().getGameOptions().mouseReleased(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
