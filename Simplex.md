
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
    // fila = número de restricción
    // coeficientes = coeficientes de las variables en esa restricción
    // b = término independiente de la restricción
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

