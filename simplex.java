import java.util.Scanner;
class InvestigacionDeOperaciones {
    private int numVariables;        
    private int numRestricciones;   
    private double[][] tabla;        
    private boolean esMaximizacion;  
    public InvestigacionDeOperaciones(int numVariables, int numRestricciones, boolean esMaximizacion) {
        this.numVariables = numVariables;
        this.numRestricciones = numRestricciones;
        this.esMaximizacion = esMaximizacion;
        // La tabla tiene (restricciones + 1) filas: todas las restricciones + fila de la función objetivo
        // Columnas = variables de decisión + variables de holgura + término independiente (lado derecho)
        tabla = new double[numRestricciones + 1][numVariables + numRestricciones + 1];
    }
    public void setFuncionObjetivo(double[] coeficientes) {
        for (int i = 0; i < numVariables; i++) {
            // Se guardan como negativos (para que el criterio de optimalidad funcione igual en max y min)
            tabla[numRestricciones][i] = -coeficientes[i];
        }
    }
    public void setRestriccion(int fila, double[] coeficientes, double b) {
        // Colocamos los coeficientes de las variables de decisión
        for (int i = 0; i < numVariables; i++) {
            tabla[fila][i] = coeficientes[i];
        }
        // Variable de holgura (1 para esa fila)
        tabla[fila][numVariables + fila] = 1;
        // Término independiente (lado derecho)
        tabla[fila][tabla[0].length - 1] = b;
    }
    private int columnaPivote() {
        int col = 0;
        double valor = tabla[numRestricciones][0];
        for (int i = 1; i < tabla[0].length - 1; i++) {
            if (tabla[numRestricciones][i] < valor) {
                valor = tabla[numRestricciones][i];
                col = i;
            }
        }
        // Si ya no hay valores negativos, se alcanzó el óptimo
        if (valor >= 0) return -1;
        return col;
    }
    private int filaPivote(int col) {
        int fila = -1;
        double minRatio = Double.MAX_VALUE;
        for (int i = 0; i < numRestricciones; i++) {
            double elem = tabla[i][col];
            if (elem > 0) { // Solo consideramos coeficientes positivos
                double ratio = tabla[i][tabla[0].length - 1] / elem;
                if (ratio < minRatio) {
                    minRatio = ratio;
                    fila = i;
                }
            }
        }
        return fila; // Retorna la fila elegida (o -1 si no existe → solución no acotada)
    }
    private void pivotear(int filaPivote, int colPivote) {
        double pivote = tabla[filaPivote][colPivote];
        // Normalizamos la fila pivote (hacemos que el pivote sea 1)
        for (int j = 0; j < tabla[0].length; j++) {
            tabla[filaPivote][j] /= pivote;
        }
        // Eliminamos los demás valores en la columna pivote
        for (int i = 0; i < tabla.length; i++) {
            if (i != filaPivote) {
                double factor = tabla[i][colPivote];
                for (int j = 0; j < tabla[0].length; j++) {
                    tabla[i][j] -= factor * tabla[filaPivote][j];
                }
            }
        }
    }
    public void resolver() {
        while (true) {
            int colPivote = columnaPivote();
            if (colPivote == -1) break; // Si no hay pivote, ya se alcanzó la solución óptima
            int filaPivote = filaPivote(colPivote);
            if (filaPivote == -1) {
                System.out.println("Solución no acotada.");
                return;
            }
            pivotear(filaPivote, colPivote); // Actualizamos la tabla con el nuevo pivote
        }
        mostrarResultado(); // Mostramos la solución final
    }
    private void mostrarResultado() {
        double[] soluciones = new double[numVariables];
        for (int i = 0; i < numVariables; i++) {
            boolean esBasica = false;
            int filaBasica = -1;
            for (int j = 0; j < numRestricciones; j++) {
                if (tabla[j][i] == 1) {
                    if (esBasica) {
                        esBasica = false;
                        break;
                    } else {
                        esBasica = true;
                        filaBasica = j;
                    }
                } else if (tabla[j][i] != 0) {
                    esBasica = false;
                    break;
                }
            }
            if (esBasica) {
                soluciones[i] = tabla[filaBasica][tabla[0].length - 1];
            } else {
                soluciones[i] = 0;
            }
        }
        double valorZ = tabla[numRestricciones][tabla[0].length - 1];
        if (!esMaximizacion) {
            valorZ = -valorZ; // En minimización invertimos el signo
        }
        System.out.println("Solución óptima:");
        for (int i = 0; i < numVariables; i++) {
            System.out.printf("x%d = %.4f\n", i + 1, soluciones[i]);
        }
        System.out.printf("Valor óptimo de la función objetivo: %.4f\n", valorZ);
    }    
}
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Método Simplex - Maximizar o Minimizar");
        System.out.println("1. Maximizar");
        System.out.println("2. Minimizar");
        System.out.print("Seleccione una opción: ");
        int opcion = sc.nextInt();
        boolean esMaximizacion = opcion == 1;
        System.out.print("Ingrese el número de variables: ");
        int numVariables = sc.nextInt();
        System.out.print("Ingrese el número de restricciones: ");
        int numRestricciones = sc.nextInt();
        InvestigacionDeOperaciones simplex = new InvestigacionDeOperaciones(numVariables, numRestricciones, esMaximizacion);
         System.out.println("Ingrese los coeficientes de la función objetivo:");
        double[] funcionObjetivo = new double[numVariables];
        for (int i = 0; i < numVariables; i++) {
            System.out.printf("Coeficiente x%d: ", i + 1);
            funcionObjetivo[i] = sc.nextDouble();
        }
        simplex.setFuncionObjetivo(funcionObjetivo);
        System.out.println("Ingrese las restricciones (coeficientes y término independiente):");
        for (int i = 0; i < numRestricciones; i++) {
            System.out.printf("Restricción %d:\n", i + 1);
            double[] coefRestriccion = new double[numVariables];
            for (int j = 0; j < numVariables; j++) {
                System.out.printf("Coeficiente x%d: ", j + 1);
                coefRestriccion[j] = sc.nextDouble();
            }
            System.out.print("Término independiente (b): ");
            double b = sc.nextDouble();
            simplex.setRestriccion(i, coefRestriccion, b);
        }
        simplex.resolver();
    }
}
        
