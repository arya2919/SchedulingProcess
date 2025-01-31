import java.util.*;

public class BankersAlgorithm {
    // Number of processes and resources
    private static final int P = 5;  // Number of Processes
    private static final int R = 4;  // Number of Resources

    // Method to calculate the Need matrix
    public static int[][] calculateNeedMatrix(int[][] allocation, int[][] max) {
        int[][] need = new int[P][R];

        for (int i = 0; i < P; i++) {
            for (int j = 0; j < R; j++) {
                need[i][j] = max[i][j] - allocation[i][j];
            }
        }

        return need;
    }

    // Method to check if a state is safe
    public static boolean isSafeState(int[] available, int[][] allocation, int[][] need) {
        // Work and Finish arrays
        int[] work = Arrays.copyOf(available, available.length);
        boolean[] finish = new boolean[P];

        // Store safe sequence
        List<Integer> safeSequence = new ArrayList<>();

        int count = 0;
        while (count < P) {
            boolean found = false;

            for (int i = 0; i < P; i++) {
                // If process is not finished and its need can be satisfied
                if (!finish[i]) {
                    boolean canAllocate = true;
                    for (int j = 0; j < R; j++) {
                        if (need[i][j] > work[j]) {
                            canAllocate = false;
                            break;
                        }
                    }

                    if (canAllocate) {
                        // Allocate resources to process
                        for (int j = 0; j < R; j++) {
                            work[j] += allocation[i][j];
                        }

                        // Mark process as finished
                        finish[i] = true;
                        safeSequence.add(i);
                        found = true;
                        count++;
                    }
                }
            }

            // If no process can be allocated
            if (!found) {
                break;
            }
        }

        // Safe if all processes are finished
        if (count == P) {
            System.out.println("Safe Sequence: " + safeSequence);
            return true;
        }

        return false;
    }

    // Method to check resource request
    public static boolean checkResourceRequest(int processId, int[] request,
                                               int[] available,
                                               int[][] allocation,
                                               int[][] need) {
        // Check if request is less than or equal to need
        for (int j = 0; j < R; j++) {
            if (request[j] > need[processId][j]) {
                System.out.println("Error: Request exceeds maximum need!");
                return false;
            }
        }

        // Check if request is less than or equal to available
        for (int j = 0; j < R; j++) {
            if (request[j] > available[j]) {
                System.out.println("Request cannot be granted immediately. System would enter unsafe state.");
                return false;
            }
        }

        // Simulate allocation
        for (int j = 0; j < R; j++) {
            available[j] -= request[j];
            allocation[processId][j] += request[j];
            need[processId][j] -= request[j];
        }

        // Check if the system remains in a safe state after allocation
        boolean isSafe = isSafeState(available, allocation, need);

        if (!isSafe) {
            // Rollback allocation if system becomes unsafe
            for (int j = 0; j < R; j++) {
                available[j] += request[j];
                allocation[processId][j] -= request[j];
                need[processId][j] += request[j];
            }
            System.out.println("Request cannot be granted. System would enter unsafe state.");
            return false;
        }

        System.out.println("Request can be granted. System remains in a safe state.");
        return true;
    }

    // Method to print matrix
    public static void printMatrix(int[][] matrix, String matrixName) {
        System.out.println(matrixName + ":");
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // Initial resource allocation state
        int[] available = {6, 7, 12, 12};  // Available resources

        int[][] allocation = {
                {0, 0, 1, 2},  // P1
                {2, 0, 0, 0},  // P2
                {0, 0, 3, 4},  // P3
                {2, 3, 5, 4},  // P4
                {0, 3, 3, 2}   // P5
        };

        int[][] max = {
                {0, 0, 1, 2},   // P1
                {2, 7, 5, 0},   // P2
                {6, 6, 5, 6},   // P3
                {4, 3, 5, 6},   // P4
                {0, 6, 5, 2}    // P5
        };

        // a) Calculate Need Matrix
        int[][] need = calculateNeedMatrix(allocation, max);
        System.out.println("a) Need Matrix:");
        printMatrix(need, "Need Matrix");

        // b) Check if system is in a safe state
        System.out.println("\nb) Safety Check:");
        boolean isSafe = isSafeState(available.clone(),
                Arrays.stream(allocation).map(int[]::clone).toArray(int[][]::new),
                Arrays.stream(need).map(int[]::clone).toArray(int[][]::new));
        System.out.println("Is system in a safe state? " + isSafe);

        // c) Resource Request for P3
        System.out.println("\nc) Resource Request for P3:");
        int[] request = {1, 0, 0, 0};  // Request for 1 instance of R2
        checkResourceRequest(2, request,
                available.clone(),
                Arrays.stream(allocation).map(int[]::clone).toArray(int[][]::new),
                Arrays.stream(need).map(int[]::clone).toArray(int[][]::new));
    }
}
