
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

