package Snake;

import java.util.Random;

public class SnakeBoard {

	int[][] board;
	int[][] cleanSlate;
	Random rng = new Random();
	SnakeSnake snake;
	SnakeCell apple;
	int gracePeriod = 0;
	
	public SnakeBoard(int BOARD_HEIGHT, int BOARD_WIDTH) {
		
		board = new int[BOARD_HEIGHT][BOARD_WIDTH];
		cleanSlate = new int[BOARD_HEIGHT][BOARD_WIDTH];
		
		for(int i = 0;i < board.length;i++) {
			
			for(int o = 0;o < board[0].length;o++) {
				
				//board[i][o] = rng.nextInt(2);
				if(i == 0 || i == board.length - 1 || o == 0 || o == board[0].length -1) {
					board[i][o] = 2;
				}else {
					board[i][o] = 0;
				}	
			}
		}
		//cleanSlate = board;
		setArrEqual(cleanSlate,board);
		snake = new SnakeSnake(board[0].length/2, board.length/2);
		
		addSnake();
		genApple();
		addApple();
		}
	
	public void nextState() {
		
	// ***************************
	// Please Check this dood
	//****************************
		
		//board = cleanSlate;
		
		
		SnakeCell nextCell = snake.nextSnakeHead();
		int x = nextCell.getX();
		int y = nextCell.getY();
		
		if(board[y][x] == 1 || board[y][x] == 2) {
			gracePeriod++;
			if(gracePeriod > 1) {
				System.out.println("You're Fired");
				gracePeriod = 2;
			}		
		}else {
			
			if(board[y][x] == 3) {
				snake.addSegment();
				genApple();
				addApple();
			}
			snake.move(nextCell);
			gracePeriod = 0;
		}
		setArrEqual(board,cleanSlate);
		addSnake();
		addApple();
	}
	
	public int getGracePeriod() {
		return gracePeriod;
	}

	public void setGracePeriod(int gracePeriod) {
		this.gracePeriod = gracePeriod;
	}

	public void addSnake() {
		
		for(int i = 0;i < snake.getSize();i++) {
			board[snake.getCell(i).getY()][snake.getCell(i).getX()] = 1;;
		}
		
	}
	
	public void setArrEqual(int[][] dest, int[][] source) {
		
		for(int i = 0;i < source.length;i++) {
			for(int o = 0; o < source[0].length;o++) {
				dest[i][o] = source[i][o];
			}
		}	
	}
	
	public void genApple() {
		
		int x,y;
		
		while(true) {
			
			x = rng.nextInt(board[0].length);
			y = rng.nextInt(board.length);
			if(board[y][x] == 0)break;
		}
		apple = new SnakeCell(x,y);
		//System.out.printf("Apple: %d,%d",y,x);
	}
	
	public void addApple() {
		board[apple.getY()][apple.getX()] = 3;
	}
	
	public void setSnakeDir(int dir) {
		
		snake.setDirection(dir);
		
	}
	
	public void addSegment() {
		
		snake.addSegment();
	}
	
	
	public int[][] getCleanSlate() {
		return cleanSlate;
	}
	
	public int getBoardHeight() {
		return board.length;
	}
	
	public int getBoardWidth() {
		return board[0].length;
	}
	
	public int getState(int x, int y) {
		return board[x][y];
	}
}
