import java.io.FileNotFoundException;

public class Main implements DS2Interface {


    /* Implement these methods */
	
	
		/*ROD CUTTING */
	
	/*For each one of the cases (recursive and dynamic) we have 2 functions. 
	 All the code is written in the second ones (recursiveRodCuttingWithLenght and dynamicRodCuttingWithLenght),
	 and the first ones (recursiveRodCutting and dynamicRodCutting) only call the other function. The reason we have 2 different function is 
	 that for the test the only input we could receive is the array, and the rodcutting algorithm 
	 needs to receive also the length of the array (to make the recursive call)*/

			/*ROD CUTTING - RECURSIVE */
	
    @Override
    public int recursiveRodCutting(int[] input) {
    	int size = input.length;
    	int x = recursiveRodCuttingWithLength(input, size); //We use the algorithm for the case we have to cut the whole rod.
    	return x;	 
    }
    
    /*In this algorithm, we calculate all the different option where we can cut the rod. 
     For each portion of the rod, we have to make the decision cut/not cut. That is why the complexity is O(2^n).*/
    
	public int recursiveRodCuttingWithLength(int [] input, int size){ 
		if (size==0){  //If we dont have any rod, the max revenue is zero.
			return 0;
		}
		int q = 0;
		for (int i=1; i<size; i++){ 
			q = Math.max(q, input[i]+recursiveRodCuttingWithLength(input,size-i));
		}
		return q;
    }
	
	/*ROD CUTTING - DYNAMIC */
	
    @Override
    public int dynamicRodCutting(int[] input) {
    	int size = input.length;
		int x = dynamicRodCuttingWithLength(input, size);
		return x;
    }
    
    /*In this case, the complexity is only O(n^2)*/
    /*Using dynamic programming, we save the result of each iteration*/
    /*When the algorithm wants to know the best option of cutting with some specific length, it use the information of a smaller lenght
     */
    
    public int dynamicRodCuttingWithLength(int [] input, int size){ 
		int [] B = new int[size+1]; 
		B[0] = 0;  //This array is saving the best way to cut for each length
		for (int j=0; j<size+1; j++){
			int q=0;
			for (int i=0; i<j; i++){
				q = Math.max(q, input[i]+B[j-i]);				
			}
			B[j]=q;
		}
		return B[size];
	}
    
    
	/*MATRIX */
    
    /*We have 4 methods that give us the maximum value between all the maxSubArrays of each orientation: Vertical, Horizontal, Diagonal and Antidiagonal*/

    //The first method is the principal function. All the methods inside it are explained below.
    @Override
    public int matrix(int[][] input) {
    	if (input.length==0){
			return 0;
		}
		else{
			int h = maxHorizontal(input); //Complexity O(n^2) - Explained below
			int v = maxVertical(input); //Complexity O(n^2)  - Explained below
			int d = maxDiagonal(input); //Complexity O(n^2)  - Explained below
			int c = maxAntiDiagonal(input); //Complexity O(n^2)  - Explained below
			return h+v+d+c;
		}
	}
    
    //The algorithm use 4 different algorithms, each one with a complexity of O(n^2). 
    //We can conclude that the complexity of the algorithm is O(n^2) (Because O(n^2)=O(4n^2))
    
    /*The function below gives the maximum value between all the maxSubArray of each line of the matrix, using a dynamic programming focus*/
    
    public int maxHorizontal(int [][]matrix){

		int size = matrix.length;
		int [] maxHorizontals = new int[size]; //This array saves the max subarray value for each line (if the matrix has 4 lines, the array will have 4 elements) 

		for (int y = 0; y<size ; y++){ //We repeat the same process for each line 
			int [] B = new int[size]; //We create a new array that will save information about our line. B[x] will be the maxsubarray ending at index x.
			B[0]= Math.max(0,matrix[y][0]); //The first element of the array will be the same than the line (but if it is negative we use zero)
			int max = B[0]; //We define max. It is the maximun value of the subarray and it starts beeing the first elements.
			for (int x=1;x<size; x++){ //We iterate through the line.
				B[x] = Math.max(0, B[x-1]+matrix[y][x]); //We add a new elements for the new array. The elements usually is the previous item for the new array added to the element of the line.
				max = Math.max(max, B[x]); //We figure out if the new element is our new maximum.
			}
			maxHorizontals[y]=max;	//We add the max of the line to the list of all the maximums.		
		}
		return maxValue(maxHorizontals); //We return the max value of the list with all the maximums.
	}
    
    /*In each iteration, when we want to get the B[r] element we use the previous elements we had obtained (dynamic programming).
     * That why the complexity of finding the maxsubarray for one line es O(n).
     * We have "n" lines, so the complexity of the algorithm is O(n^2)
     */
    
    
    
    /*The function below gives the maximum value between all the maxSubArray of the matrix seeing it in an vertical way 
    We use the same algorithm from above, but first we transpose the matrix. Doing this, if we calculate the maxsubarrays of the lines, 
    we are actually calculating the maxsubarrays of the columns */
   
	public int maxVertical(int [][]matrix){
		int size = matrix.length;
		int [] maxVerticales = new int[size];
		int [][] matrixT = matrixTrans(matrix);
		return maxHorizontal(matrixT);
	}
	
	/*We know that the maxHorizontal() method is O(n^2). Here should be the same, except we have to tranpose the matrix first.
	 * As is explained below, transpose a matrix has O(n^2) complexity. But we know that O(2*n^2) = O(n^2).
	 * So, we know the complexity of this method is O(n^2)
	 */
	
	
	 /*The function below returns the maximun value between all the maxSubArray of each column of the matrix.*/

	public int maxDiagonal(int [][] matriz){

		int size = matriz.length;
		int pointerY = 0; //This pointer will go through the matrix across the y-axis
		int pointerX = 0; //This pointer will go through the matrix across the x-axis
		int [] maxDiagonals = new int[2*size]; //This array will save all the maximum values of each subarray.

		//First we get the maxsubarray of the upper diagonals.
		while(pointerY<size){ 
			int [] b = new int[size];
			b[0] = Math.max(0, matriz[0][pointerY]);
			int maximo = b[0];
			for (int t=1;t<size-pointerY;t++){ //We iterate moving through the matrix in a diagonal direction
				//Its important to notice that the "t<size-pointer" rule makes that in each iteration we are going through only one diagonal line.
				b[t]=Math.max(0, b[t-1]+matriz[t][t+pointerY]);
				maximo = Math.max(maximo, b[t]);
			}
			maxDiagonals[pointerY]=maximo;
			pointerY++;
		}
		//Then we get the maxsubarray of the lower diagonals.
		while (pointerX<size){
			int [] b = new int[size];
			b[0] = Math.max(0, matriz[pointerX][0]);
			int maximo = b[0];
			for (int t=1;size<size-pointerX;t++){
				b[t]=Math.max(0, b[t-1]+matriz[t+pointerX][t]);
				maximo = Math.max(maximo, b[t]);
			}
			maxDiagonals[pointerY+pointerX]= maximo;	
			pointerX++;
		}

		return maxValue(maxDiagonals);
	}
	
	/*We can see that the mechanism is very similar with the previous cases. 
	 * Here we have to find the maxsubarray of 2*n-1 lines (the number of diagonal a matrix has). Each search has a complexity of O(n).
	 * However,  we now that O((2n-1)*n)) is still O(n^2). So the complexity is O(n^2)
	 */
	
	/*The function below returns the maximun value between all the maxSubArray of the matrix seeing it in an antidiagonal way.
	We use the same algorithm from the function above, but only after a little modification of our matrix:
	 We invert the order of the elements for each line. Doing that, we get the same information of the function above but instead 
	 of the diagonals, we use the diagonals from the other side */

	public int maxAntiDiagonal (int [][] matrix){ 
		int size = matrix.length;
		int [][] newMatrix = new int [size][size];
		for (int y=0; y<size; y++){
			for (int x=0;x<size;x++){
				newMatrix[y][x] = matrix[y][size-x-1];
			}
		}
		return maxDiagonal(newMatrix);
    }
	
	/*Here we can see that the complexity is also O(n^2), because the only difference with the previous method is 
	 * the transformation of the matrix. Although the complexity of the transformation is O(n^2) (explained below), 
	 * the complexity of the algorithm is still O(n^2) (because  O(2n^2) = O(n^2). 
	 */
	

	 /*The function below returns the maximum value of an array.
	  In the worst, best and average case is O(n) (it always has to go through all elements)*/
	
	public int maxValue(int [] list){
		int max = list[0];
		for (int i=0;i<list.length; i++){
			if (list[i]>max){
				max = list[i];
			}
		}
		return max;
	}
	
	/*The function below receive a matrix and return the respective transposed matrix.
	 The complexity is O(n^2) in all cases because it has to go through all elements of the matrix
	 */
	
	public int [][] matrixTrans (int [][] matrix){
		int [][] matrixTrans = new int [matrix.length][matrix.length];
		for (int i=0; i < matrix.length; i++) {
			for (int j=0; j < matrix[i].length; j++) { 
				matrixTrans[j][i] = matrix[i][j];
			}
		}
		return matrixTrans;
	}


    /* BEGIN UTIL FUNCTION. DO NOT TOUCH */



    void start(String assignment, String file) throws FileNotFoundException {
        switch (assignment) {
            case "rod": {
                int result = recursiveRodCutting(Util.readInput(file));
                System.out.printf("%d\n", result);
            }
                break;
            case "dynrod": {
                int result = dynamicRodCutting(Util.readInput(file));
                System.out.printf("%d\n", result);
                break;
            }
            case "matrix": {
                int result = matrix(Util.readMatrix(file));
                System.out.printf("%d\n", result);
                break;
            }
            default:
                System.out.printf("Invalid assignment provided: %s\n", assignment);
                printHelp(null);
                System.exit(1);
        }
    }

    static void printArray(int[] array) {
        for (int e: array) {
            System.out.printf("%d ", e);
        }
    }

    static void printMatrix(int[][] matrix) {
        for (int[] l: matrix) {
            printArray(l);
            System.out.printf("\n");
        }
    }

    static void printHelp(String[] argv) {
        System.out.printf("Usage: java -jar DS2.jar <assignment> [<input_file>]\n");
        System.out.printf("\t<algorithm>: rod, dynrod, matrix\n");
        System.out.printf("E.g.: java -jar DS2.jar rod example.txt\n");
    }

    public static void main(String argv[]) {
        if (argv.length == 0) {
            printHelp(argv);
            System.exit(1);
        }
        try {
            (new Main()).start(argv[0], argv.length < 2 ? null : argv[1]);
        } catch (FileNotFoundException e) {
            System.out.printf("File not found: %s", e.getMessage());
        }

    }

}
