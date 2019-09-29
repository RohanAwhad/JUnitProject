package io.rohanawhad;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class GameplayTest {

	Gameplay gamePlay;
	
	@BeforeEach
	public void init() {
		gamePlay = new Gameplay();
	}
	
	
	@Test
	@DisplayName("GamePlay initialization testing")
	public void initialSetupTest() {
		
		//score
		int expectedScore = 0;
		int actualScore = gamePlay.getScore();
		
		//ball x pos
		int expectedBallposX = 120;
		int acutalBallposX = gamePlay.getBallposX();
		
		//ball y pos
		int expectedBallposY = 350;
		int acutalBallposY = gamePlay.getBallposY();
		
		// ball x dir
		int expectedBallXdir = -1;
		int actualBallXdir = gamePlay.getBallXdir();
		
		// ball y dir
		int expectedBallYdir = -2;
		int actualBallYdir = gamePlay.getBallYdir();
		
		// player x pos
		int expectedPlayerXpos = 310;
		int actualPlayerXpos = gamePlay.getplayerX();
		
		//total bricks
		int expectedTotalBricks = 21;
		int actualTotalBricks = gamePlay.getTotalBricks();
		
		// Game on or not
		boolean expectedPlay = false;
		boolean actualPlay = gamePlay.isGameOn();
		
		assertAll(
				() -> assertEquals(expectedScore, actualScore, () -> "Score should be " + expectedScore + " at the start but returned " + actualScore),
				() -> assertEquals(expectedBallposX, acutalBallposX, () -> "Ball X position should be " + expectedBallposX + " at the start but returned " + acutalBallposX),
				() -> assertEquals(expectedBallposY, acutalBallposY, () -> "Ball Y position should be " + expectedBallposY + " at the start but returned " + acutalBallposY),
				() -> assertEquals(expectedBallXdir, actualBallXdir, () -> "Ball's X direction should be " + expectedBallXdir + " at the start but returned " + actualBallXdir),
				() -> assertEquals(expectedBallYdir, actualBallYdir, () -> "Ball's Y direction should be " + expectedBallYdir + " at the start but returned " + actualBallYdir),
				() -> assertEquals(expectedPlayerXpos, actualPlayerXpos, () -> "Player's X position should be " + expectedPlayerXpos + " at the start but returned " + actualPlayerXpos),
				() -> assertEquals(expectedTotalBricks, actualTotalBricks, () -> "Total bricks should be " + expectedTotalBricks + " at the start but returned " + actualTotalBricks),
				() -> assertEquals(expectedPlay, actualPlay, () -> "Gameplay should be " + expectedPlayerXpos + " at the start but returned " + actualPlayerXpos)
				);
	}
	
	
	@Nested
	class PlayerMovement{
		
		@SuppressWarnings("deprecation")
		@Test
		@DisplayName("Testing basic right movement")
		public void rightMovementTesting() {
			
			int expectedPlayerXpos =  gamePlay.getplayerX() + 20;
			
			gamePlay.keyPressed(new KeyEvent(new Component() {} , 0, 01, 0, KeyEvent.VK_RIGHT));
			int actualPlayerXpos = gamePlay.getplayerX();		
			
			assertEquals(expectedPlayerXpos, actualPlayerXpos, () -> "Player's X position should be " + expectedPlayerXpos + " after pressing right key but returned " + actualPlayerXpos);
		}
		
		@SuppressWarnings("deprecation")
		@Test
		@DisplayName("Testing basic left movement")
		public void leftMovementTesting() {
			
			int expectedPlayerXpos = gamePlay.getplayerX() - 20;
			
			gamePlay.keyPressed(new KeyEvent(new Component() {} , 0, 01, 0, KeyEvent.VK_LEFT));
			int actualPlayerXpos = gamePlay.getplayerX();		
			
			assertEquals(expectedPlayerXpos, actualPlayerXpos, () -> "Player's X position should be " + expectedPlayerXpos + " after pressing left key the start but returned " + actualPlayerXpos);
		}
		
		@SuppressWarnings("deprecation")
		@Test
		@DisplayName("Testing wall right movement")
		public void rightWallMovementTesting() {
			
			gamePlay.setPlayerX(597);
			int expectedPlayerXpos =  gamePlay.getplayerX();
			
			gamePlay.keyPressed(new KeyEvent(new Component() {} , 0, 01, 0, KeyEvent.VK_RIGHT));
			int actualPlayerXpos = gamePlay.getplayerX();		
			
			assertEquals(expectedPlayerXpos, actualPlayerXpos, () -> "Player's X position should be " + expectedPlayerXpos + " after pressing right key and being at right end the start but returned " + actualPlayerXpos);
		}
		
		@SuppressWarnings("deprecation")
		@Test
		@DisplayName("Testing wall left movement")
		public void leftWallMovementTesting() {
			
			gamePlay.setPlayerX(10);
			int expectedPlayerXpos = gamePlay.getplayerX();
			
			gamePlay.keyPressed(new KeyEvent(new Component() {} , 0, 01, 0, KeyEvent.VK_LEFT));
			int actualPlayerXpos = gamePlay.getplayerX();		
			
			assertEquals(expectedPlayerXpos, actualPlayerXpos, () -> "Player's X position should be " + expectedPlayerXpos + " after pressing left key and being at left end the start but returned " + actualPlayerXpos);
		}
	}
	
	@Nested
	class BallMovement{
		
		@Test
		@DisplayName("Testing direction change after hitting paddle")
		public void hittingPaddle() {
			
			gamePlay.setBallposX(gamePlay.getplayerX() + 5);
			gamePlay.setBallposY(531);
			gamePlay.setBallYdir(2);
			gamePlay.setPlay(true);
			
			int expectedBallXdir = gamePlay.getBallXdir();
			int expectedBallYdir = -1 * gamePlay.getBallYdir();
			
			assumeTrue(gamePlay.getBallYdir() > 0);
			assumeTrue(gamePlay.isGameOn());
			gamePlay.actionPerformed(new ActionEvent(new Component() {}, 0, " "));
			
			int actualBallXdir = gamePlay.getBallXdir();
			int actualBallYdir = gamePlay.getBallYdir();
			
			assertEquals(expectedBallXdir, actualBallXdir, () -> "Ball's X direction should be " + expectedBallXdir + " after hitting the paddle but returned " + actualBallXdir);
			assertEquals(expectedBallYdir, actualBallYdir, () -> "Ball's Y direction should be " + expectedBallYdir + " after hitting the paddle but returned " + actualBallYdir);
			
		}
		
		@Test
		@DisplayName("Testing direction change after hitting left wall")
		public void hittingLeftWall() {
			
			gamePlay.setBallposX(0);
			gamePlay.setBallXdir(-1);
			gamePlay.setPlay(true);
			
			int expectedBallXdir = -1 * gamePlay.getBallXdir();
			int expectedBallYdir = gamePlay.getBallYdir();
			
			assumeTrue(gamePlay.getBallXdir() < 0);
			assumeTrue(gamePlay.isGameOn());
			gamePlay.actionPerformed(new ActionEvent(new Component() {}, 0, ""));
			
			int actualBallXdir = gamePlay.getBallXdir();
			int actualBallYdir = gamePlay.getBallYdir();
			
			assertEquals(expectedBallXdir, actualBallXdir, () -> "Ball's X direction should be " + expectedBallXdir + " after hitting the left wall but returned " + actualBallXdir);
			assertEquals(expectedBallYdir, actualBallYdir, () -> "Ball's Y direction should be " + expectedBallYdir + " after hitting the left wall but returned " + actualBallYdir);
			
		}
		
		@Test
		@DisplayName("Testing direction change after hitting right wall")
		public void hittingRightWall() {
			
			gamePlay.setBallposX(679);
			gamePlay.setBallXdir(1);
			gamePlay.setPlay(true);
			
			int expectedBallXdir = -1 * gamePlay.getBallXdir();
			int expectedBallYdir = gamePlay.getBallYdir();
			
			assumeTrue(gamePlay.getBallXdir() > 0);
			assumeTrue(gamePlay.isGameOn());
			gamePlay.actionPerformed(new ActionEvent(new Component() {}, 0, " "));
			
			int actualBallXdir = gamePlay.getBallXdir();
			int actualBallYdir = gamePlay.getBallYdir();
			
			assertEquals(expectedBallXdir, actualBallXdir, () -> "Ball's X direction should be " + expectedBallXdir + " after hitting the right wall but returned " + actualBallXdir);
			assertEquals(expectedBallYdir, actualBallYdir, () -> "Ball's Y direction should be " + expectedBallYdir + " after hitting the right wall but returned " + actualBallYdir);
			
		}
		
		@Test
		@DisplayName("Testing direction change after hitting top wall")
		public void hittingTopWall() {
			
			gamePlay.setBallposY(-1);
			gamePlay.setBallYdir(-2);
			gamePlay.setPlay(true);
			
			int expectedBallXdir = gamePlay.getBallXdir();
			int expectedBallYdir = -1 * gamePlay.getBallYdir();
			
			assumeTrue(gamePlay.getBallXdir() < 0);
			assumeTrue(gamePlay.isGameOn());
			gamePlay.actionPerformed(new ActionEvent(new Component() {}, 0, " "));
			
			int actualBallXdir = gamePlay.getBallXdir();
			int actualBallYdir = gamePlay.getBallYdir();
			
			assertEquals(expectedBallXdir, actualBallXdir, () -> "Ball's X direction should be " + expectedBallXdir + " after hitting the top wall but returned " + actualBallXdir);
			assertEquals(expectedBallYdir, actualBallYdir, () -> "Ball's Y direction should be " + expectedBallYdir + " after hitting the top wall but returned " + actualBallYdir);
			
		}
		
		
		
	}
	

	@Nested
	class HittingBricks{

		@Test
		@DisplayName("Testing direction change after hitting brick from top")
		public void hittingBricksFromTop() {

			//Setting position and direction
			gamePlay.setPlay(true);
			gamePlay.setBallposY(31);
			gamePlay.setBallposX(85);
			gamePlay.setBallYdir(2);

			// Setting expectations
			int expectedBricks = gamePlay.getTotalBricks() - 1;
			int expectedBallYdir = -1 * gamePlay.getBallYdir();
			int expectedBallXdir = gamePlay.getBallXdir();
			int expectedScore = 5 + gamePlay.getScore();

			// Setting assumptions
			assumeTrue(gamePlay.getBallYdir() > 0);
			assumeTrue(gamePlay.isGameOn());

			//Taking action
			gamePlay.actionPerformed(new ActionEvent(new Component() {}, 0, " "));

			//Getting values after taking action
			int actualBricks = gamePlay.getTotalBricks(); 
			int actualBallYdir = gamePlay.getBallYdir();
			int actualBallXdir = gamePlay.getBallXdir();
			int actualScore = gamePlay.getScore();

			assertAll(
				() -> assertEquals(expectedBricks, actualBricks, () -> "Count of bricks should be " + expectedBricks + " but returned " + actualBricks),
				() -> assertEquals(expectedBallXdir, actualBallXdir, () -> "Ball's X direction should be " + expectedBallXdir + " but returned " + actualBallXdir),
				() -> assertEquals(expectedBallYdir, actualBallYdir, () -> "Ball's Y direction should be " + expectedBallYdir + " but returned " + actualBallYdir),
				() -> assertEquals(expectedScore, actualScore, () -> "Score should be " + expectedScore + " but returned " + actualScore)
				);
		}

		@Test
		@DisplayName("Testing direction change after hitting brick from left side")
		public void hittingBricksFromLeft() {

			//Setting position and direction
			gamePlay.setPlay(true);
			gamePlay.setBallposY(55);
			gamePlay.setBallposX(61);
			gamePlay.setBallXdir(1);

			// Setting expectations
			int expectedBricks = gamePlay.getTotalBricks() - 1;
			int expectedBallYdir = gamePlay.getBallYdir();
			int expectedBallXdir = -1 * gamePlay.getBallXdir();
			int expectedScore = 5 + gamePlay.getScore();

			// Setting assumptions
			assumeTrue(gamePlay.getBallXdir() > 0);
			assumeTrue(gamePlay.isGameOn());

			//Taking action
			gamePlay.actionPerformed(new ActionEvent(new Component() {}, 0, " "));

			//Getting values after taking action
			int actualBricks = gamePlay.getTotalBricks(); 
			int actualBallYdir = gamePlay.getBallYdir();
			int actualBallXdir = gamePlay.getBallXdir();
			int actualScore = gamePlay.getScore();

			assertAll(
				() -> assertEquals(expectedBricks, actualBricks, () -> "Count of bricks should be " + expectedBricks + " but returned " + actualBricks),
				() -> assertEquals(expectedBallXdir, actualBallXdir, () -> "Ball's X direction should be " + expectedBallXdir + " but returned " + actualBallXdir),
				() -> assertEquals(expectedBallYdir, actualBallYdir, () -> "Ball's Y direction should be " + expectedBallYdir + " but returned " + actualBallYdir),
				() -> assertEquals(expectedScore, actualScore, () -> "Score should be " + expectedScore + " but returned " + actualScore)
				);
		}

		@Test
		@DisplayName("Testing direction change after hitting brick from right side")
		public void hittingBricksFromRight() {

			//Setting position and direction
			gamePlay.setPlay(true);
			gamePlay.setBallposY(65);
			gamePlay.setBallposX(541);
			gamePlay.setBallXdir(-1);

			// Setting expectations
			int expectedBricks = gamePlay.getTotalBricks() - 1;
			int expectedBallYdir = gamePlay.getBallYdir();
			int expectedBallXdir = -1 * gamePlay.getBallXdir();
			int expectedScore = 5 + gamePlay.getScore();

			// Setting assumptions
			assumeTrue(gamePlay.getBallXdir() < 0);
			assumeTrue(gamePlay.isGameOn());

			//Taking action
			gamePlay.actionPerformed(new ActionEvent(new Component() {}, 0, " "));

			//Getting values after taking action
			int actualBricks = gamePlay.getTotalBricks(); 
			int actualBallYdir = gamePlay.getBallYdir();
			int actualBallXdir = gamePlay.getBallXdir();
			int actualScore = gamePlay.getScore();

			assertAll(
				() -> assertEquals(expectedBricks, actualBricks, () -> "Count of bricks should be " + expectedBricks + " but returned " + actualBricks),
				() -> assertEquals(expectedBallXdir, actualBallXdir, () -> "Ball's X direction should be " + expectedBallXdir + " but returned " + actualBallXdir),
				() -> assertEquals(expectedBallYdir, actualBallYdir, () -> "Ball's Y direction should be " + expectedBallYdir + " but returned " + actualBallYdir),
				() -> assertEquals(expectedScore, actualScore, () -> "Score should be " + expectedScore + " but returned " + actualScore)
				);
		}

	}
	
	@Nested
	class GameOver{
		
		@Test
		@DisplayName("Testing game lose condition")
		public void gamelose() {
			
			gamePlay.setBallposY(575);
			assumeTrue(gamePlay.getTotalBricks() > 0);
			gamePlay.setPlay(true);
			boolean game = gamePlay.isGameLost();
			boolean play = gamePlay.isGameOn();
			
			assertTrue(game, () -> "Game should be lost but it is " + game);
			assertFalse(play, () -> "Game should be over but it is" + play);
			
		}
		
		@Test
		@DisplayName("Testing game win condition")
		public void gameWin() {
			
			gamePlay.setTotalBricks(0);
			assumeTrue(gamePlay.getBallposY() < 570);
			gamePlay.setPlay(true);
			boolean game = gamePlay.isGameWon();
			boolean play = gamePlay.isGameOn();
			
			assertTrue(game, () -> "Game should be won but it is " + game);
			assertFalse(play, () -> "Game should be over but it is" + play);
			
		}
		
		@Test
		@DisplayName("Testing game restart action")
		public void restartGame() {
			
			assumeFalse(gamePlay.isGameOn());
			gamePlay.keyPressed(new KeyEvent(new Component() {	}, 0, 01, 0, KeyEvent.VK_ENTER));
			assertTrue(gamePlay.isGameOn(), "Game should restart but it did not");
			
		}
		
	}
			
}
