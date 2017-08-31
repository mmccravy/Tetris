/**
 * This class is responsible for game logic
 */
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JFrame;

public class playGame extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final long timeScreen = 1000L / 50L;	
	private static final int pieceNum = Piece.values().length; //num pieces	

	private boolean isPaused, isNewGame, hasGameEnded;
	private int level, score, actualColumn, actualRow, actualRotation, droppingRest;
	private float gameSpeed;
	
	private Random random;
	private Time logicTimer;
	private GameBoard board;
	private Menu side;
	private Piece currentPiece, nextPiece;

	private playGame(){
		super("Tetris");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		this.board = new GameBoard(this);	//board and menu
		this.side = new Menu(this);

		add(board, BorderLayout.CENTER);
		add(side, BorderLayout.EAST);

		addKeyListener(new KeyAdapter(){
			
			@Override
			public void keyPressed(KeyEvent e) {
				
				switch(e.getKeyCode()) {
			
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
					if(!isPaused && droppingRest == 0) {
						logicTimer.setCyclesPerSecond(25.0f);
					}
					break;

				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:	
					if(!isPaused && board.canPieceInsert(currentPiece, actualColumn - 1, actualRow, actualRotation)) {
						actualColumn--;
					}
					break;

				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:	
					if(!isPaused && board.canPieceInsert(currentPiece, actualColumn + 1, actualRow, actualRotation)) {
						actualColumn++;
					}
					break;

				case KeyEvent.VK_Q:
					if(!isPaused) {
						rotatePiece((actualRotation == 0) ? 3 : actualRotation - 1);
					}
					break;

				case KeyEvent.VK_E:
					if(!isPaused) {
						rotatePiece((actualRotation == 3) ? 0 : actualRotation + 1);
					}
					break;

				case KeyEvent.VK_P:
					if(!hasGameEnded && !isNewGame) {
						isPaused = !isPaused;
						logicTimer.setPaused(isPaused);
					}
					break;

				case KeyEvent.VK_ENTER:
					if(hasGameEnded || isNewGame) {
						gameReset();
					}
					break;
				
				}
			}
			@Override
			public void keyReleased(KeyEvent e){
				switch(e.getKeyCode()){
				
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:	
					logicTimer.setCyclesPerSecond(gameSpeed);
					logicTimer.reset();
					break;
				}
			}	
		});

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	/**
	 * Gets the column of the current piece
	 * @return -- column
	 */
	public int getColumn(){
		return actualColumn;
	}
	/**
	 * Gets the row of the current piece
	 * @return -- row
	 */
	public int getRow(){
		return actualRow;
	}
	/**
	 * Gets score.
	 * @return -- score.
	 */
	public int getScore(){
		return score;
	}	
	/**
	 * Gets level
	 * @return -- level
	 */
	public int getLevel(){
		return level;
	}
	/**
	 * Gets the rotation of the current piece
	 * @return -- rotation
	 */
	public int getRotation(){
		return actualRotation;
	}
	/**
	 * Gets the piece
	 * @return -- piece type.
	 */
	public Piece getPiece(){
		return currentPiece;
	}
	/**
	 * Gets the next of piece
	 * @return -- next piece
	 */
	public Piece getNextPiece(){
		return nextPiece;
	}
	/**
	 * Checks to see if the game is paused
	 * @return -- if the game is paused
	 */
	public boolean isPaused(){
		return isPaused;
	}	
	/**
	 * Checks to see if the game is over
	 * @return -- if the game is over
	 */
	public boolean hasGameEnded(){
		return hasGameEnded;
	}
	/**
	 * Checks to see if it is a new game
	 * @return -- if we are in a new game
	 */
	public boolean isNewGame(){
		return isNewGame;
	}	

	private void startGame() {			//game loop
		this.random = new Random();
		this.isNewGame = true;
		this.gameSpeed = 1.0f;

		this.logicTimer = new Time(gameSpeed);		//game hold until user starts
		logicTimer.setPaused(true);
		
		while(true){
			long start = System.nanoTime();
			logicTimer.update();
			
			if(logicTimer.hasCycleFinished()) 
				updateGame();
			if(droppingRest > 0)
				droppingRest--;			
			renderGame();

			long delta = (System.nanoTime() - start) / 1000000L;
			if(delta < timeScreen){
				try
				{
					Thread.sleep(timeScreen - delta);
				} 
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * New Render
	 */
	private void renderGame(){
		board.repaint();
		side.repaint();
	}
	private void updateGame() {
		if(board.canPieceInsert(currentPiece, actualColumn, actualRow + 1, actualRotation))
			actualRow++;
		else
		{
			board.addGamePiece(currentPiece, actualColumn, actualRow, actualRotation);

			int wipe = board.lineScan();		//1 = 100pts, 2 = 200pts, 3 = 400pts, 4 = 800pts
			if(wipe > 0){
				score += 50 << wipe;
			}

			gameSpeed += 0.035f;		//speed and timer
			logicTimer.setCyclesPerSecond(gameSpeed);
			logicTimer.reset();
			droppingRest = 25;

			level = (int)(gameSpeed * 1.70f);		//difficulty

			pieceGenerate();
		}		
	}	
	/**
	 *Resets game and variables
	 */
	private void gameReset(){
		this.level = 1;
		this.score = 0;
		this.gameSpeed = 1.0f;
		this.nextPiece = Piece.values()[random.nextInt(pieceNum)];
		this.isNewGame = false;
		this.hasGameEnded = false;
		
		board.wipe();
		logicTimer.reset();
		logicTimer.setCyclesPerSecond(gameSpeed);
		pieceGenerate();
	}	
	/**
	 * Generates a new piece and resets
	 */
	private void pieceGenerate(){
		this.currentPiece = nextPiece;
		this.actualColumn = currentPiece.getColumnGenerate();
		this.actualRow = currentPiece.getRowGenerate();
		this.actualRotation = 0;
		this.nextPiece = Piece.values()[random.nextInt(pieceNum)];

		if(!board.canPieceInsert(currentPiece, actualColumn, actualRow, actualRotation))	//game over if generate point is invalid
		{
			this.hasGameEnded = true;
			logicTimer.setPaused(true);
		}		
	}
	/**
	 * Sets the rotation to newRotation.
	 * @param newRotation -- the rotation of the new piece.
	 */
	private void rotatePiece(int newRotation){
		int newColumn = actualColumn;
		int newRow = actualRow;
	
		int left = currentPiece.getSpaceLeft(newRotation);		//spacing outside of the pieces
		int right = currentPiece.getSpaceRight(newRotation);
		int top = currentPiece.getSpaceTop(newRotation);
		int bottom = currentPiece.getSpaceBottom(newRotation);

		if(actualColumn < -left)
			newColumn -= actualColumn - left;
		else if(actualColumn + currentPiece.getDimension() - right >= GameBoard.numOfColums)
			newColumn -= (actualColumn + currentPiece.getDimension() - right) - GameBoard.numOfColums + 1;

		if(actualRow < -top)
			newRow -= actualRow - top;
		else if(actualRow + currentPiece.getDimension() - bottom >= GameBoard.totalNumRows)
			newRow -= (actualRow + currentPiece.getDimension() - bottom) - GameBoard.totalNumRows + 1;

		if(board.canPieceInsert(currentPiece, newColumn, newRow, newRotation))
		{
			actualRotation = newRotation;
			actualRow = newRow;
			actualColumn = newColumn;
		}
	}
	/**
	 * main
	 */
	public static void main(String[] args){
		playGame tetris = new playGame();
		tetris.startGame();
	}
}
