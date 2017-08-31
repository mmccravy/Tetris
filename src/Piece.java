/**
 *Class that handles the piece functions an game piece functionality
 */
import java.awt.Color;

public enum Piece{					//tetris pieces(alphabetically)
	/**
	 * I-PIECE
	 * 	#
	 * 	#
	 * 	#
	 * 	#
	 */
	I(new Color(GameBoard.minColorShade, GameBoard.maxColorShade, GameBoard.maxColorShade), 4, 4, 1, new boolean[][]{
		{
			false,	false,	false,	false,
			true,	true,	true,	true,
			false,	false,	false,	false,
			false,	false,	false,	false,
		},
		{
			false,	false,	true,	false,
			false,	false,	true,	false,
			false,	false,	true,	false,
			false,	false,	true,	false,
		},
		{
			false,	false,	false,	false,
			false,	false,	false,	false,
			true,	true,	true,	true,
			false,	false,	false,	false,
		},
		{
			false,	true,	false,	false,
			false,	true,	false,	false,
			false,	true,	false,	false,
			false,	true,	false,	false,
		}
	}),
	/**
	 * J-PIECE
	 * 	#
	 * 	#
	 * ##
	 */
	J(new Color(GameBoard.minColorShade, GameBoard.minColorShade, GameBoard.maxColorShade), 3, 3, 2, new boolean[][]{
		{
			true,	false,	false,
			true,	true,	true,
			false,	false,	false,
		},
		{
			false,	true,	true,
			false,	true,	false,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	true,
			false,	false,	true,
		},
		{
			false,	true,	false,
			false,	true,	false,
			true,	true,	false,
		}
	}),	
	/**
	 * L-PIECE
	 * 	#
	 * 	#
	 * 	##
	 */
	L(new Color(GameBoard.maxColorShade, 127, GameBoard.minColorShade), 3, 3, 2, new boolean[][]{
		{
			false,	false,	true,
			true,	true,	true,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	false,
			false,	true,	true,
		},
		{
			false,	false,	false,
			true,	true,	true,
			true,	false,	false,
		},
		{
			true,	true,	false,
			false,	true,	false,
			false,	true,	false,
		}
	}),
	/**
	 *O-PIECE
	 * ##
	 * ##
	 */
	O(new Color(GameBoard.maxColorShade, GameBoard.maxColorShade, GameBoard.minColorShade), 2, 2, 2, new boolean[][]{
		{
			true,	true,
			true,	true,
		},
		{
			true,	true,
			true,	true,
		},
		{	
			true,	true,
			true,	true,
		},
		{
			true,	true,
			true,	true,
		}
	}),
	/**
	 * S-PIECE
	 * 	##
	 * ##
	 */
	S(new Color(GameBoard.minColorShade, GameBoard.maxColorShade, GameBoard.minColorShade), 3, 3, 2, new boolean[][]{
		{
			false,	true,	true,
			true,	true,	false,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	true,
			false,	false,	true,
		},
		{
			false,	false,	false,
			false,	true,	true,
			true,	true,	false,
		},
		{
			true,	false,	false,
			true,	true,	false,
			false,	true,	false,
		}
	}),	
	/**
	 * T-PIECE
	 * 	#
	 * ###
	 */
	T(new Color(128, GameBoard.minColorShade, 128), 3, 3, 2, new boolean[][]{
		{
			false,	true,	false,
			true,	true,	true,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	true,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	true,
			false,	true,	false,
		},
		{
			false,	true,	false,
			true,	true,	false,
			false,	true,	false,
		}
	}),
	/**
	 * Z-PIECE
	 * ##
	 * 	##
	 */
	Z(new Color(GameBoard.maxColorShade, GameBoard.minColorShade, GameBoard.minColorShade), 3, 3, 2, new boolean[][] {
		{
			true,	true,	false,
			false,	true,	true,
			false,	false,	false,
		},
		{
			false,	false,	true,
			false,	true,	true,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	false,
			false,	true,	true,
		},
		{
			false,	true,	false,
			true,	true,	false,
			true,	false,	false,
		}
	});

	private Color base;
	private Color lighter;
	private Color darker;
	private int columnGenerate;
	private int rowGenerate;
	private int dimension;
	private int numOfRows;
	private int numOfColumns;
	private boolean[][] tiles;
	
	/**
	 * Creates a new Piece.
	 * @param color -- beginning color
	 * @param dimension -- dimensions of the tiles array
	 * @param columns -- number of columns
	 * @param rows -- number of rows
	 * @param tiles -- actual tiles
	 */
	private Piece(Color color, int dimension, int columns, int rows, boolean[][] tiles){
		this.base = color;
		this.lighter = color.brighter();
		this.darker = color.darker();
		this.dimension = dimension;
		this.tiles = tiles;
		this.numOfColumns = columns;
		this.numOfRows = rows;
		
		this.columnGenerate = 5 - (dimension >> 1);
		this.rowGenerate = getSpaceTop(0);
	}
	/**
	 * Gets the dimension
	 * @return -- dimension
	 */
	public int getDimension(){
		return dimension;
	}
	/**
	 * Gets the number of columns
	 * @return -- number of columns
	 */
	public int getNumberOfColumns(){
		return numOfColumns;
	}
	/**
	 * Gets the number of rows
	 * @return -- number of rows
	 */
	public int getNumberOfRows(){
		return numOfRows;
	}
	/**
	 * Gets the base color
	 * @return -- base color
	 */
	public Color getColor(){
		return base;
	}
	/**
	 * Gets the light shade color
	 * @return -- lighter color.
	 */
	public Color getLighter(){
		return lighter;
	}
	/**
	 * Gets the dark shade color
	 * @return -- darker color
	 */
	public Color getDarker(){
		return darker;
	}
	/**
	 * Gets the generated column
	 * @return -- generated column
	 */
	public int getColumnGenerate(){
		return columnGenerate;
	}
	/**
	 * Gets the generated row
	 * @return -- generated row
	 */
	public int getRowGenerate(){
		return rowGenerate;
	}
	/**
	 * Checks to see a tile is present considering the coordinates and rotation
	 * @param x -- x coordinate
	 * @param y -- y coordinate
	 * @param rotation -- rotation
	 * @return if the tile is present
	 */
	public boolean isTilePresent(int x, int y, int rotation){
		return tiles[rotation][y * dimension + x];
	}
	/**
	 * The method represents the number of empty columns on the left for the rotation
	 * @param rotation -- rotation
	 * @return this targeted space
	 */
	public int getSpaceLeft(int rotation){

		for(int x = 0; x < dimension; x++)
		{
			for(int y = 0; y < dimension; y++)
			{
				if(isTilePresent(x, y, rotation))
					return x;
			}
		}
		return -1;
	}
	/**
	 * The method represents the number of empty columns on the bottom for the rotation
	 * @param rotation -- rotation
	 * @return this targeted space
	 */
	public int getSpaceBottom(int rotation){
		for(int y = dimension - 1; y >= 0; y--)
		{
			for(int x = 0; x < dimension; x++)
			{
				if(isTilePresent(x, y, rotation)) 
					return dimension - y;
			}
		}
		return -1;
	}
	/**
	 * The method represents the number of empty columns on the right for the rotation
	 * @param rotation -- rotation
	 * @return this targeted space
	 */
	public int getSpaceRight(int rotation){
		for(int x = dimension - 1; x >= 0; x--)
		{
			for(int y = 0; y < dimension; y++)
			{
				if(isTilePresent(x, y, rotation)) 
					return dimension - x;
			}
		}
		return -1;
	}
	/**
	 * The method represents the number of empty columns on the top for the rotation
	 * @param rotation -- rotation
	 * @return this targeted space
	 */
	public int getSpaceTop(int rotation){
		for(int y = 0; y < dimension; y++)
		{
			for(int x = 0; x < dimension; x++)
			{
				if(isTilePresent(x, y, rotation)) 
					return y;
			}
		}
		return -1;
	}
}