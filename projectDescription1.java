//2241016135_DOS_EndTermProject
import java.util.*;

class Process {
    int pid;            // Process ID
    int arrivalTime;    // Arrival time
    int burstTime;      // CPU burst time
    int remainingTime;  // Remaining burst time (for RR)
    int completionTime; // Completion time
    int turnaroundTime; // Turnaround time
    int waitingTime;    // Waiting time
    int responseTime;   // Response time
    boolean firstRun;   // Flag to track first execution

    public Process(int pid, int arrivalTime, int burstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.firstRun = false;
    }
}

public class projectDescription1 {
    private static final int MAX_PROCESSES = 100;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            // Menu
            System.out.println("\nProcess Scheduling Algorithms");
            System.out.println("1. First Come First Served (FCFS)");
            System.out.println("2. Round Robin (RR)");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            // Exit condition
            if (choice == 3) {
                System.out.println("Exiting program...");
                break;
            }

            // Input number of processes
            System.out.print("Enter the number of processes: ");
            int n = scanner.nextInt();

            if (n <= 0 || n > MAX_PROCESSES) {
                System.out.println("Invalid number of processes. Please try again.");
                continue;
            }

            // Create process array
            Process[] processes = new Process[n];

            // Input process details
            for (int i = 0; i < n; i++) {
                System.out.println("\nEnter details for Process " + (i+1) + ":");
                System.out.print("Arrival Time: ");
                int arrivalTime = scanner.nextInt();
                System.out.print("Burst Time: ");
                int burstTime = scanner.nextInt();

                processes[i] = new Process(i+1, arrivalTime, burstTime);
            }

            // Algorithm selection
            switch (choice) {
                case 1:
                    fcfsScheduling(processes);
                    break;
                case 2:
                    // Input time quantum for Round Robin
                    System.out.print("Enter Time Quantum: ");
                    int timeQuantum = scanner.nextInt();
                    roundRobinScheduling(processes, timeQuantum);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    // FCFS Scheduling Algorithm
    private static void fcfsScheduling(Process[] processes) {
        // Sort processes based on arrival time
        Arrays.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));

        int currentTime = 0;

        // Execute processes
        for (Process process : processes) {
            // Wait if process hasn't arrived
            if (currentTime < process.arrivalTime) {
                currentTime = process.arrivalTime;
            }

            // First execution
            process.responseTime = currentTime - process.arrivalTime;

            // Update completion time
            process.completionTime = currentTime + process.burstTime;

            // Update current time
            currentTime = process.completionTime;
        }

        // Calculate metrics
        calculateMetrics(processes);

        // Print Gantt Chart
        System.out.println("\nFCFS Scheduling Gantt Chart:");
        printGanttChart(processes);
    }

    // Round Robin Scheduling Algorithm
    private static void roundRobinScheduling(Process[] processes, int timeQuantum) {
        // Sort processes based on arrival time
        Arrays.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));

        int currentTime = 0;
        int completedProcesses = 0;
        Queue<Process> readyQueue = new LinkedList<>();

        // Reset remaining times and first run flags
        for (Process process : processes) {
            process.remainingTime = process.burstTime;
            process.firstRun = false;
        }

        // Add initially arrived processes to queue
        for (Process process : processes) {
            if (process.arrivalTime == 0) {
                readyQueue.offer(process);
            }
        }

        while (completedProcesses < processes.length) {
            if (readyQueue.isEmpty()) {
                // No processes in queue, advance time
                currentTime++;

                // Check for newly arrived processes
                for (Process process : processes) {
                    if (process.remainingTime > 0 && process.arrivalTime <= currentTime) {
                        readyQueue.offer(process);
                    }
                }
                continue;
            }

            Process currentProcess = readyQueue.poll();

            // Mark first response time
            if (!currentProcess.firstRun) {
                currentProcess.firstRun = true;
                currentProcess.responseTime = currentTime - currentProcess.arrivalTime;
            }

            // Execute process
            int execTime = Math.min(currentProcess.remainingTime, timeQuantum);

            currentTime += execTime;
            currentProcess.remainingTime -= execTime;

            // Check for newly arrived processes during execution
            for (Process process : processes) {
                if (process != currentProcess &&
                        process.remainingTime > 0 &&
                        process.arrivalTime <= currentTime &&
                        !process.firstRun) {
                    readyQueue.offer(process);
                }
            }

            // If process not complete, add back to queue
            if (currentProcess.remainingTime > 0) {
                readyQueue.offer(currentProcess);
            } else {
                // Mark completion time
                currentProcess.completionTime = currentTime;
                completedProcesses++;
            }
        }

        // Calculate metrics
        calculateMetrics(processes);

        // Print Gantt Chart
        System.out.printf("\nRound Robin Scheduling Gantt Chart (Time Quantum = %d):\n", timeQuantum);
        printGanttChart(processes);
    }

    // Calculate waiting, turnaround, and response times
    private static void calculateMetrics(Process[] processes) {
        float totalWaitingTime = 0, totalTurnaroundTime = 0, totalResponseTime = 0;

        for (Process process : processes) {
            // Turnaround time = Completion time - Arrival time
            process.turnaroundTime = process.completionTime - process.arrivalTime;

            // Waiting time = Turnaround time - Burst time
            process.waitingTime = process.turnaroundTime - process.burstTime;

            // Accumulate total times
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.turnaroundTime;
            totalResponseTime += process.responseTime;
        }

        // Print metrics
        System.out.println("\nPerformance Metrics:");
        System.out.printf("Average Waiting Time: %.2f ms\n", totalWaitingTime / processes.length);
        System.out.printf("Average Turnaround Time: %.2f ms\n", totalTurnaroundTime / processes.length);
        System.out.printf("Average Response Time: %.2f ms\n", totalResponseTime / processes.length);
    }

    // Print Gantt Chart
    private static void printGanttChart(Process[] processes) {
        // Print top border
        System.out.print("+");
        for (Process process : processes) {
            System.out.print("----P" + process.pid + "----+");
        }
        System.out.println();

        // Print time
        System.out.print("0");
        for (Process process : processes) {
            System.out.printf("%13d", process.completionTime);
        }
        System.out.println();
    }

}
