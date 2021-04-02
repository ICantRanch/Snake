package Snake;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class SnakeMain extends Application{

	int WIN_WIDTH = 700;
	int WIN_HEIGHT = 700;
	GraphicsContext gc;
	Canvas c;
	Timer t;
	TimerTask tt;
	Label lb;
	LinkedList<Integer> keyQueue = new LinkedList<>();
	
	
	SnakeBoard board = new SnakeBoard(20,30);
	
	int boardHeight;
	int boardWidth;
	int borderSize = 0;;
	int cellSize = 25;
	int iterSize = borderSize + cellSize;
	double snakeSize = 0.7;
	int snakecellSize =  (int)(Math.sqrt(snakeSize)*cellSize);
	
	int snakeOffset = (cellSize - snakecellSize)/2;
	
	int framesBetweenStates = 0;
	double renderFps = 30;
	double gameFps = 7;
	int perframeJump = cellSize/((int)renderFps / (int)Math.round(gameFps));
	
	SnakeCell prevLast = board.snake.getCell(0);
	SnakeCell prevFirst = board.snake.getCell(0);
	
	//iron out kinks in keyqueue
	
	
	public void start(Stage stage) throws Exception {
		System.out.println(snakecellSize);
		
		boardHeight = board.getBoardHeight();
		boardWidth = board.getBoardWidth();
		
		WIN_HEIGHT = ((iterSize)*(boardHeight-2)) + borderSize;
		WIN_WIDTH = ((iterSize)*(boardWidth-2)) + borderSize;
		
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root);
		c = new Canvas(WIN_WIDTH,WIN_HEIGHT);
		gc = c.getGraphicsContext2D();
		root.setCenter(c);
		
		HBox hb = new HBox();
		hb.setSpacing(5);
		hb.setPadding(new Insets(5,5,5,5));
		root.setTop(hb);
		hb.setAlignment(Pos.CENTER);
		
		lb = new Label();
		
		hb.getChildren().addAll(lb);
		
		myTimer();
		
		stage.setWidth(c.getWidth() + 20);
		stage.setHeight(c.getHeight() + 100);
		stage.setTitle("Snake");
		stage.setScene(scene);
		stage.show();
		
		stage.setOnCloseRequest(event ->{
			t.cancel();
		});
		
		EventHandler<KeyEvent> eh = new EventHandler<KeyEvent>() {
			
			public void handle(KeyEvent ke) {
				
				if(ke.getCode() == KeyCode.SPACE) {
					board.addSegment();
				}
				if(ke.getCode() == KeyCode.UP) {
					if(keyQueue.size() < 2) {
						keyQueue.add(1);
					}else {
						keyQueue.set(0, keyQueue.get(1));
						keyQueue.set(1, 1);
					}
				}
				if(ke.getCode() == KeyCode.DOWN) {
					if(keyQueue.size() < 2) {
						keyQueue.add(3);
					}else {
						keyQueue.set(0, keyQueue.get(1));
						keyQueue.set(1, 3);
					}	
					}
				if(ke.getCode() == KeyCode.LEFT) {
					if(keyQueue.size() < 2) {
						keyQueue.add(4);
					}else {
						keyQueue.set(0, keyQueue.get(1));
						keyQueue.set(1, 4);
					}	
				}
				if(ke.getCode() == KeyCode.RIGHT) {
					if(keyQueue.size() < 2) {
						keyQueue.add(2);
					}else {
						keyQueue.set(0, keyQueue.get(1));
						keyQueue.set(1, 2);
					}	
				}	
			}			
		};
		scene.addEventHandler(KeyEvent.KEY_PRESSED, eh);
		
		
	}
	
	public void myTimer() {
        TimerTask tt = new TimerTask() {

            @Override
            public void run() {
                System.out.println("Life of a MAC");
                gamett();
            }
        };
        t = new Timer();
        t.schedule(tt, 0, 60);

    }
    public void gamett() {
    	
        TimerTask tt = new TimerTask() {

            @Override
            public void run() {
            	
            	
            	if(keyQueue.size() > 0) {
            		board.snake.setDirection(keyQueue.poll());
            	}
            	prevLast = board.snake.getCell(board.snake.getSize()-1);
            	board.nextState();
            	System.out.printf("Snake head: %d,%d\n",board.snake.getCell(0).getY(),board.snake.getCell(0).getX());
            	framesBetweenStates = 0;
            	//System.out.println("Gaming");
            }
        };
        TimerTask tt2 = new TimerTask() {
        	@Override
        	public void run() {
        		render();
        		
        		framesBetweenStates++;
        	}
        };
        t.cancel();
        t = new Timer();
        t.schedule(tt, 0, (long) (1000/gameFps));
        t.schedule(tt2, 0, (long) (1000/renderFps));
    }
    
    public void adjustSnake(SnakeSnake snake) {
    	
    	int xOffset = 0;
    	int yOffset = 0;
    	int adjustment = framesBetweenStates*perframeJump;
    	if(snake.getDirection() == 1) {
    		yOffset = -(adjustment);
    	}else if(snake.getDirection() == 2) {
    		xOffset = adjustment;
    	}else if(snake.getDirection() == 3) {
    		yOffset = adjustment;
    	}else if(snake.getDirection() == 4) {
    		xOffset = -(adjustment);
    	}
    	Paint temp = gc.getFill();
    	if(snake.getSize() < 2) {
    		gc.setFill(Color.BLACK);
    		drawPoint(snake.getCell(0).getX(),snake.getCell(0).getY());
    		
    		if(snake.getDirection() == 1) {
    			drawPointOffset(snake.getCell(0).getX(),snake.getCell(0).getY(),0,2*snakeOffset+1);
    			gc.setFill(temp);
    			drawPointOffset(snake.getCell(0).getX(),snake.getCell(0).getY()+1,xOffset,yOffset);
    		}else if(snake.getDirection() == 2) {
    			drawPointOffset(snake.getCell(0).getX(),snake.getCell(0).getY(),-(2*snakeOffset+1),0);
    			gc.setFill(temp);
    			drawPointOffset(snake.getCell(0).getX()-1,snake.getCell(0).getY(),xOffset,yOffset);
    		}else if(snake.getDirection() == 3) {
    			drawPointOffset(snake.getCell(0).getX(),snake.getCell(0).getY(),0,-(2*snakeOffset+1));
    			gc.setFill(temp);
    			drawPointOffset(snake.getCell(0).getX(),snake.getCell(0).getY()-1,xOffset,yOffset);     
    		}else if(snake.getDirection() == 4) {
    			drawPointOffset(snake.getCell(0).getX(),snake.getCell(0).getY(),2*snakeOffset+1,0);
    			gc.setFill(temp);
    			drawPointOffset(snake.getCell(0).getX()+1,snake.getCell(0).getY(),xOffset,yOffset);
    		}else {
    			gc.setFill(temp);
    			drawPoint(snake.getCell(0).getX(),snake.getCell(0).getY());
    		}
    	}else {
    		gc.setFill(Color.BLACK);
    		drawPoint(snake.getCell(0).getX(),snake.getCell(0).getY());
    		gc.setFill(temp);
    		drawPointOffset(snake.getCell(1).getX(),snake.getCell(1).getY(),xOffset,yOffset);
    	}
    	
    	xOffset = 0;
    	yOffset = 0;
    	
    	int dir;
    	if(snake.getSize() < 2) {
    		dir = snake.getDirection();
    	}else {
    		dir = findDirection((prevLast),snake.getCell(snake.getSize()-1));
    	}
    	
    	if(dir == 1) {
    		yOffset = -(adjustment);
    		if(snake.getSize() > 1) {
    			drawPointOffset(snake.getCell(snake.getSize()-1).getX(),snake.getCell(snake.getSize()-1).getY(),0,2*snakeOffset+1);
    		}
    		drawPointOffset(prevLast.getX(),prevLast.getY(),xOffset,yOffset);
    	}else if(dir == 2) {
    		xOffset = adjustment;
    		if(snake.getSize() > 1) {
    			drawPointOffset(snake.getCell(snake.getSize()-1).getX(),snake.getCell(snake.getSize()-1).getY(),-(2*snakeOffset+1),0);
    		}
    		drawPointOffset(prevLast.getX(),prevLast.getY(),xOffset,yOffset);
    	}else if(dir == 3) {
    		yOffset = adjustment;
    		if(snake.getSize() > 1) {
    			drawPointOffset(snake.getCell(snake.getSize()-1).getX(),snake.getCell(snake.getSize()-1).getY(),0,-(2*snakeOffset+1));
    		}
    		drawPointOffset(prevLast.getX(),prevLast.getY(),xOffset,yOffset);
    	}else if(dir == 4) {
    		xOffset = -(adjustment);
    		if(snake.getSize() > 1) {
    			drawPointOffset(snake.getCell(snake.getSize()-1).getX(),snake.getCell(snake.getSize()-1).getY(),2*snakeOffset+1,0);
    		}
    		drawPointOffset(prevLast.getX(),prevLast.getY(),xOffset,yOffset);
    	}
    }
    
    public int findDirection(SnakeCell first, SnakeCell second) {
    	int x1 = first.getX();
    	int y1 = first.getY();
    	int x2 = second.getX();
    	int y2 = second.getY();
    	int dir = 0;
    	
    	if(x1 != x2) {
    		//Left or Right
    		if(x1 > x2) {
    			//Right
    			dir = 4;
    		}else {
    			//Left
    			dir = 2;
    		}    		
    	}else if(y1 != y2) {
    		//Up or Down
    		if(y1 > y2) {
    			//Up
    			dir = 1;
    		}else {
    			//Down
    			dir = 3;
    		}
    	}
    	return dir;
    }
    
    public void renderwithBorders() {
    	
    	gc.setFill(Color.GREY);
    	gc.fillRect(0, 0, c.getWidth(), c.getHeight());
    	
    	int xPos = borderSize, yPos = borderSize;
    	
    	for(int i = 0; i < boardHeight; i++) {
			
			for(int o = 0;o < boardWidth; o++) {
    	
				if(board.getState(i,o) == 1) {
					gc.setFill(Color.WHITE);		
				}else if(board.getState(i,o) == 2){
					gc.setFill(Color.DARKGRAY);
				}else if(board.getState(i,o) == 3){
					gc.setFill(Color.LIME);
				}else {
					gc.setFill(Color.BLACK);
				}
				
				gc.fillRect(xPos, yPos, cellSize, cellSize);
				
				xPos += iterSize;
			}	
			xPos = borderSize;
			yPos += iterSize;
		}		
    }
    
    public void render() {
    	gc.setFill(Color.BLACK);
    	gc.fillRect(0, 0, c.getWidth(), c.getHeight());
    	
    	int xPos = borderSize, yPos = borderSize;
    	
    	for(int i = 1; i < (boardHeight-1); i++) {
			
			for(int o = 1;o < (boardWidth-1); o++) {
    	
				gc.setFill(Color.BLACK);
				
				//Only connect if segments are adjacent on the snake
				
				/*
				if(board.getState(i,o) == 1) {
					gc.setFill(Color.WHITE);
					if(board.getState(i+1,o) == 1) {
						gc.fillRect(xPos+snakeOffset, yPos+snakeOffset, snakecellSize, cellSize );
					}
					if(board.getState(i,o+1) == 1) {
						gc.fillRect(xPos+snakeOffset, yPos+snakeOffset, cellSize, snakecellSize );
					}	
				}
				*/
				
				if(board.getState(i,o) == 3){
				gc.setFill(Color.LIME);
				}
				gc.fillRect(xPos+snakeOffset, yPos+snakeOffset, snakecellSize, snakecellSize);
				xPos += iterSize;
				}	
			xPos = borderSize;
			yPos += iterSize;
			}
    	//drawSnake();
    	int grace = board.getGracePeriod();
    	if(grace == 1) {
    		gc.setFill(Color.ORANGE);
    	}else if(grace > 1) {
    		gc.setFill(Color.RED);
    	}else {
    		gc.setFill(Color.WHITE);
    	}
    	
    	
    	drawSnakeTP(getSnakeTP(board.snake));
    	drawPoint(board.snake.getCell(0).getX(),board.snake.getCell(0).getY());
    	drawPoint(board.snake.getCell(board.snake.getSize()-1).getX(),board.snake.getCell(board.snake.getSize()-1).getY());
    	adjustSnake(board.snake);
    }	
    
    public LinkedList<SnakeCell>  getSnakeTP(SnakeSnake snake) {
    	
    	//Try printing tp list to find if x's and y's are in the same plane, otherwise drawBetween2Points wont work
    	
    	LinkedList<SnakeCell> tp = new LinkedList<SnakeCell>();
    	tp.add(board.snake.getCell(0));
    	
    	int beforeX,beforeY,afterX,afterY;
    	
    	
    	for(int i = 1; i < (snake.getSize()-1);i++) {
    		
    		beforeX = snake.getCell(i-1).getX();
    		beforeY = snake.getCell(i-1).getY();
    		afterX = snake.getCell(i+1).getX();
    		afterY = snake.getCell(i+1).getY();
    		
    		if(beforeX != afterX && beforeY != afterY) {
    			tp.add(snake.getCell(i));
    		}
    	}
    	tp.addLast(board.snake.getCell(board.snake.getSize()-1));
    	
    	return tp;
    }
    
    public void drawSnakeTP(LinkedList<SnakeCell> tp) {
    	
    	for(int i = 0;i < (tp.size()-1);i++) {
    		drawBetween2Points(tp.get(i).getX(),tp.get(i).getY(),tp.get(i+1).getX(),tp.get(i+1).getY());
    		
    	}
    }
    
    public void drawPoint(int x,int y) {
    	
    	gc.fillRect(((x-1)*iterSize)+snakeOffset, ((y-1)*iterSize)+snakeOffset, snakecellSize, snakecellSize);
    	
    }
    
    public void drawPointOffset(int x,int y,int xOffset,int yOffset) {
    	
    	gc.fillRect(((x-1)*iterSize)+snakeOffset + xOffset, ((y-1)*iterSize)+snakeOffset + yOffset, snakecellSize, snakecellSize);
    	
    }
    
    public void drawBetween2Points(int x1, int y1, int x2, int y2) {
    	
    	//Works, get started on implementing it with tp
    	//System.out.println("Drawing");
    	
    		 
    	if(x1 != x2) {
    		if(x1 > x2) {
   				//left
   				//System.out.println("Left");
   				myfillRect(((x1-1)*iterSize)+snakeOffset+snakecellSize,((y1-1)*iterSize)+snakeOffset,-((Math.abs(x1-x2))*iterSize+snakecellSize),snakecellSize);
   			}else {
   				//right
    			//System.out.println("Right");
    			myfillRect(((x1-1)*iterSize)+snakeOffset,((y1-1)*iterSize)+snakeOffset,((Math.abs(x1-x2))*iterSize+snakecellSize),snakecellSize);
    			
    		}
    			
   		}else if(y1 != y2) {    			
    		if(y1 - y2 > 0) {
    			//up
   				//System.out.println("Up");
   				myfillRect(((x1-1)*iterSize)+snakeOffset,((y1-1)*iterSize)+snakeOffset+snakecellSize,snakecellSize,-((Math.abs(y1-y2))*iterSize+snakecellSize));
   			}else {
   				//down
   				//System.out.println("Down");
   				myfillRect(((x1-1)*iterSize)+snakeOffset,((y1-1)*iterSize)+snakeOffset,snakecellSize,((Math.abs(y1-y2))*iterSize+snakecellSize));
    		}
   		}
    	
    }
    
    public void drawSnake() {
    	
    	//Finish drawing blank board with apples, then draw snake all at once
    	
    	//iterate through snake to find turning points maybe
    	//turning point if both x and y change within 2 spaces 
    	//practice your typing
    	
    	//store position of segment and next,connect
    	
    	//**************
    	//   OBSELETE
    	//**************
    	
    	int seg1X;
		int seg1Y;
		int seg2X = 0;
		int seg2Y = 0;
		int width;
		int height;
		int xPos;
		int yPos;
		
    	
    	for(int i = 0;i < board.snake.getSize();i++) {
    		
    		gc.setFill(Color.WHITE);
    		
    		seg1X = board.snake.getCell(i).getX();
    		seg1Y = board.snake.getCell(i).getY();
    		if(i < board.snake.getSize()-1) {
    			seg2X = board.snake.getCell(i+1).getX();
    		seg2Y = board.snake.getCell(i+1).getY();
    		}
    		
    		xPos = ((seg1X-1)*iterSize)+snakeOffset;
    		yPos = ((seg1Y-1)*iterSize)+snakeOffset;
    		
    		if(board.snake.getSize() > 0) {
 
    		if(seg1X != seg2X) {
    			if(seg1X - seg2X > 0) {
    				//left
    				width = -iterSize;
    				//System.out.println("Left");
    			}else {
    				//right
    				width = iterSize;
    				//System.out.println("Right");
    			}
    			//System.out.println("Drawing");
    			//System.out.printf("xPos: %d, yPos: %d, width: %d, height: %d\n",xPos,yPos ,width , snakecellSize);
    			myfillRect(xPos,yPos ,width , snakecellSize );
    			
    		}else if(seg1Y != seg2Y) {
    			
    			if(seg1Y - seg2Y > 0) {
    				//up
    				height = -iterSize;
    				//System.out.println("Up");
    			}else {
    				//down
    				height = iterSize;
    				//System.out.println("Down");
    			}
    			//System.out.println("Drawing");
    			//System.out.printf("xPos: %d, yPos: %d, width: %d, height: %d\n",xPos,yPos ,snakecellSize , height );
    			myfillRect(xPos,yPos ,snakecellSize , height );
    		}
    		}else {
    			myfillRect(xPos,yPos,snakecellSize,snakecellSize);
    		}
    	}	
    }
    
    public void myfillRect( int x, int y, int width, int height){
        if(width <0)
            x-=Math.abs(width);
        if(height <0)
            y-=Math.abs(height);

        gc.fillRect(x,y,Math.abs(width), Math.abs(height));
    }
    
    public void printArr(int[][] Arr) {
    	
    	for(int i = 0; i < Arr.length; i++) {
			
			for(int o = 0;o < Arr[0].length; o++) {
    	
				System.out.print(Arr[i][o]);
			}	
			System.out.print("\n");
		}		
    }

	
	public static void main(String[] args) {
		
		launch(args);

	}

}
