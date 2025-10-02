import java.util.Scanner;
class InvestigacionDeOperaciones {
    private int numVariables;        // Número de variables de decisión
    private int numRestricciones;    // Número de restricciones
    private double[][] tabla;        // Tabla del método simplex
    private boolean esMaximizacion;  // Indica si es un problema de maximización o minimización
   
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
        
       
}
