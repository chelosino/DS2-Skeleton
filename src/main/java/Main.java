import java.io.FileNotFoundException;

public class Main implements DS2Interface {


    /* Implement these methods */

    @Override
    public int recursiveRodCutting(int[] input) {
    	int size = input.length;
    	int x = recursiveRodCuttingWithLength(input, size);
    	return x;
    	
    }
    
	public int recursiveRodCuttingWithLength(int [] input, int size){ 
		if (size==0){ 
			return 0;
		}
		int q = 0;
		for (int i=1; i<size; i++){ 
			q = Math.max(q, input[i]+recursiveRodCuttingWithLength(input,size-i));
		}
		return q;
    }


    @Override
    public int dynamicRodCutting(int[] input) {
    	int size = input.length;
		int x = dynamicRodCuttingWithLength(input, size);
		return x;
    }
    
    public int dynamicRodCuttingWithLength(int [] input, int size){ 
		int [] B = new int[size+1]; 
		B[0] = 0; 
		for (int j=0; j<size+1; j++){
			int q=0;
			for (int i=0; i<j; i++){
				q = Math.max(q, input[i]+B[j-i]);				
			}
			B[j]=q;
		}
		return B[size];
	}


    @Override
    public int matrix(int[][] input) {
    	if (input.length==0){
			return 0;
		}
		else{
			int h = maxHorizontal(input);
			int v = maxVertical(input);
			int d = maxDiagonal(input);
			int c = maxAntiDiagonal(input);
			return h+v+d+c;
		}
	}
    
    public int maxHorizontal(int [][]matrix){

		int size = matrix.length;
		int [] maxHorizontals = new int[size];

		for (int y = 0; y<size ; y++){
			int [] B = new int[size];
			B[0]= Math.max(0,matrix[y][0]);
			int max = B[0];
			for (int x=1;x<size; x++){
				B[x] = Math.max(0, B[x-1]+matrix[y][x]); 
				max = Math.max(max, B[x]); 
			}
			maxHorizontals[y]=max;			
		}
		return maxValue(maxHorizontals);
	}

	public int maxVertical(int [][]matrix){
		int size = matrix.length;
		int [] maxVerticales = new int[size];
		int [][] matrixT = matrixTrans(matrix);
		return maxHorizontal(matrixT);

	}

	public int maxDiagonal(int [][] matriz){//ESTE ES EL QUE FUNCIONA

		int size = matriz.length;
		int pointerY = 0;
		int pointerX = 0;
		int [] maxDiagonals = new int[2*size];

		while(pointerY<size){

			int [] b = new int[size];
			b[0] = Math.max(0, matriz[0][pointerY]);
			int maximo = b[0];
			for (int t=1;t<size-pointerY;t++){
				b[t]=Math.max(0, b[t-1]+matriz[t][t+pointerY]);
				maximo = Math.max(maximo, b[t]);
			}
			maxDiagonals[pointerY]=maximo;
			pointerY++;
		}

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
	
	public int maxValue(int [] list){
		int max = list[0];
		for (int i=0;i<list.length; i++){
			if (list[i]>max){
				max = list[i];
			}
		}
		return max;
	}
	
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
