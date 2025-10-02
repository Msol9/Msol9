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