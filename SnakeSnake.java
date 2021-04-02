package Snake;

import java.util.LinkedList;

public class SnakeSnake {

	LinkedList<SnakeCell> snake = new LinkedList<SnakeCell>();
	
	int direction; //0=none, 1=up, 2=right, 3=down, 4=left
	
	
	public SnakeSnake(int x, int y) {
		
		snake.add(new SnakeCell(x,y));
		direction = 0;
	}
	
	
	public void move(SnakeCell nextHead){
		
		SnakeCell temp = snake.get(0);
		SnakeCell temp2;
		snake.set(0,nextHead);
		
		for(int i = 1; i < snake.size();i++) {
			
			temp2 = snake.get(i);
			snake.set(i, temp);
			temp = temp2;
			
			
		}
		
		
	}
	
	public int getDirection() {
		return direction;
	}


	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public void addSegment() {
		
		snake.addLast(snake.getLast());
	}


	//Called by board and checked if move will result in termination, then sent to move
	public SnakeCell nextSnakeHead() {
		
		SnakeCell nextHead;
			
		if(direction == 1) {
			nextHead = new SnakeCell(snake.get(0).getX(),snake.get(0).getY()-1);
		}else if(direction == 2) {
			nextHead = new SnakeCell(snake.get(0).getX()+1,snake.get(0).getY());
		}else if(direction == 3) {
			nextHead = new SnakeCell(snake.get(0).getX(),snake.get(0).getY()+1);
		}else if(direction == 4) {
			nextHead = new SnakeCell(snake.get(0).getX()-1,snake.get(0).getY());
		}else {
			nextHead = snake.get(0);
		}
		
		return nextHead;
	}
	
	
	public int getSize() {
		return snake.size();
	}
	
	public SnakeCell getCell(int i) {
		return snake.get(i);
	}

	
}
