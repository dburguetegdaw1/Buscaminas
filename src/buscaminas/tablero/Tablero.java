package buscaminas.tablero;

/**
 *  
 * 
 * @author  
 * @version  
 */
public class Tablero
{
    private char[][] tableroVisible;
    private char[][] tableroOculto;
    private boolean[][] casillaDescubierta;
    private int[][] casillasConBombaCerca;
    private int bombas = 0;
    private int Largo;
    private int Ancho;
    /**
     * Si introduces números por debajo de 3 o mayor a 52
     * se creará un tablero de 10x10
     */
    public Tablero(int Ancho, int Largo, int b){
        if((Ancho < 3 || Largo < 3) || (Ancho > 52 || Largo > 52)){
            Ancho = 10;
            Largo = 10;
        }
        this.Largo = Largo;
        this.Ancho = Ancho;
        bombas = b;
        tableroVisible = new char[Largo][Ancho];
        tableroOculto = new char[Largo][Ancho];
        casillaDescubierta = new boolean[Largo][Ancho];
        casillasConBombaCerca = new int[Largo][Ancho];
        for(int i = 0; i < tableroVisible.length; i++){
            for(int j = 0; j < tableroVisible[i].length; j++){
                tableroVisible[i][j] = '~';
            }
        }
        llenarTableroConBombas(Ancho, Largo, b);
        colocarNumeros();
    }
    
    public char[][] getTableroVisible() {
		return tableroVisible;
	}

	public char[][] getTableroOculto() {
		return tableroOculto;
	}
    
    public boolean[][] getCasillaDescubierta() {
		return casillaDescubierta;
	}
    
    public String cantBombas() {
    	return "/" + bombas;
    }

	private void llenarTableroConBombas(int m, int n, int b){
    	
        for(int i = 0; i < b; i++){
            colocarBomba(m, n);
        }
    }
    
    public void colocarBomba(int m, int n){
        int casillaX = (int)(Math.random()*m);
        int casillaY = (int)(Math.random()*n);
        if(tableroOculto[casillaY][casillaX] != 'X'){
            tableroOculto[casillaY][casillaX] = 'X';
        }
        else{
            colocarBomba(m, n);
        }
    }
    
    public void vaciarNums() {
    	for (int i = 0; i < casillaDescubierta.length; i++) {
			for (int j = 0; j < casillaDescubierta.length; j++) {
				if (tableroOculto[i][j] != 'X') { 
					tableroOculto[i][j] = '9';
				}
			}
		}
    }

	public void setTableroOculto(int i, int j) {
		this.tableroOculto[i][j] = '9';
	}

	public void setCasillasConBombaCerca() {
		this.casillasConBombaCerca = new int[Ancho][Largo];
	}

	public void colocarNumeros(){
        for(int i = 0; i < tableroVisible.length; i++){
            for(int j = 0; j < tableroVisible[i].length; j++){
                if(tableroOculto[i][j] == 'X'){
                    if(i-1 >= 0 && j-1 >= 0 && tableroOculto[i-1][j-1] != 'X'){
                        casillasConBombaCerca[i-1][j-1]++;
                    }
                    if(i-1 >= 0 && tableroOculto[i-1][j] != 'X'){
                        casillasConBombaCerca[i-1][j]++;
                    }
                    if(i-1 >= 0 && j+1 < tableroOculto[i].length && tableroOculto[i-1][j+1] != 'X'){
                        casillasConBombaCerca[i-1][j+1]++;
                    }
                    if(j-1 >= 0 && tableroOculto[i][j-1] != 'X'){
                        casillasConBombaCerca[i][j-1]++;
                    }
                    if(j+1 < tableroOculto[i].length && tableroOculto[i][j+1] != 'X'){
                        casillasConBombaCerca[i][j+1]++;
                    }
                    if(i+1 < tableroOculto.length && j-1 >= 0 && tableroOculto[i+1][j-1] != 'X'){
                        casillasConBombaCerca[i+1][j-1]++;
                    }
                    if(i+1 < tableroOculto.length && tableroOculto[i+1][j] != 'X'){
                        casillasConBombaCerca[i+1][j]++;
                    }
                    if(i+1 < tableroOculto.length && j+1 < tableroOculto[i].length && tableroOculto[i+1][j+1] != 'X'){
                        casillasConBombaCerca[i+1][j+1]++;
                    }
                }
            }
        }
        for(int i = 0; i < tableroVisible.length; i++){
            for(int j = 0; j < tableroVisible[i].length; j++){
                switch(casillasConBombaCerca[i][j]){
                    case 1:
                        tableroOculto[i][j] = '1';
                        break;
                    case 2:
                        tableroOculto[i][j] = '2';
                        break;
                    case 3:
                        tableroOculto[i][j] = '3';
                        break;
                    case 4:
                        tableroOculto[i][j] = '4';
                        break;
                    case 5:
                        tableroOculto[i][j] = '5';
                        break;
                    case 6:
                        tableroOculto[i][j] = '6';
                        break;
                    case 7:
                        tableroOculto[i][j] = '7';
                        break;
                    case 8:
                        tableroOculto[i][j] = '8';
                        break;
                }
                if(tableroOculto[i][j] == '\u0000') {
                	tableroOculto[i][j] = '9';
                }
            }
        }
    }
    
    public void descubrirCasilla(int y, int x){
        if(!casillaDescubierta[y][x]){
            casillaDescubierta[y][x] = true;
        }
        if(tableroOculto[y][x] == '9' && casillaDescubierta[y][x]){
            if(y-1 >= 0 && x-1 >= 0 && tableroOculto[y - 1][x - 1] == '9'){
                if(!casillaDescubierta[y - 1][x - 1])
                    descubrirCasilla(y-1, x-1);
                casillaDescubierta[y - 1][x - 1] = true;
            }
            else if(y-1 >= 0 && x-1 >= 0){
                casillaDescubierta[y - 1][x - 1] = true;
            }
            if(y-1 >= 0 && tableroOculto[y - 1][x] == '9'){
                if(!casillaDescubierta[y - 1][x])
                    descubrirCasilla(y-1, x);
                casillaDescubierta[y - 1][x] = true;
            }
            else if(y-1 >= 0){
                casillaDescubierta[y - 1][x] = true;
            }
            if(y-1 >= 0 && x+1 < tableroOculto[y].length && tableroOculto[y - 1][x + 1] == '9'){
                if(!casillaDescubierta[y - 1][x + 1])
                    descubrirCasilla(y-1, x+1);
                casillaDescubierta[y - 1][x + 1] = true;
            }
            else if(y-1 >= 0 && x+1 < tableroOculto[y].length){
                casillaDescubierta[y - 1][x + 1] = true;
            }
            if(x-1 >= 0 && tableroOculto[y][x - 1] == '9'){
                if(!casillaDescubierta[y][x - 1])
                    descubrirCasilla(y, x-1);
                casillaDescubierta[y][x - 1] = true;
            }
            else if(x-1 >= 0){
                casillaDescubierta[y][x - 1] = true;
            }
            if(x+1 < tableroOculto[y].length && tableroOculto[y][x + 1] == '9'){
                if(!casillaDescubierta[y][x + 1])
                    descubrirCasilla(y, x+1);
                casillaDescubierta[y][x + 1] = true;
            }
            else if(x+1 < tableroOculto[y].length){
                casillaDescubierta[y][x + 1] = true;
            }
            if(y+1 < tableroOculto.length && x-1 >= 0 && tableroOculto[y + 1][x - 1] == '9'){
                if(!casillaDescubierta[y + 1][x - 1])
                    descubrirCasilla(y+1, x-1);
                casillaDescubierta[y + 1][x - 1] = true;
            }
            else if(y+1 < tableroOculto.length && x-1 >= 0){
                casillaDescubierta[y + 1][x - 1] = true;
            }
            if(y+1 < tableroOculto.length && tableroOculto[y + 1][x] == '9'){
                if(!casillaDescubierta[y + 1][x])
                    descubrirCasilla(y+1, x);
                casillaDescubierta[y + 1][x] = true;
            }
            else if(y+1 < tableroOculto.length){
                casillaDescubierta[y + 1][x] = true;
            }
            if(y+1 < tableroOculto.length && x+1 < tableroOculto[y].length && tableroOculto[y + 1][x + 1] == '9'){
                if(!casillaDescubierta[y + 1][x + 1])
                    descubrirCasilla(y+1, x+1);
                casillaDescubierta[y + 1][x + 1] = true;
            }
            else if(y+1 < tableroOculto.length && x+1 < tableroOculto[y].length){
                casillaDescubierta[y + 1][x + 1] = true;
            }
        }
    }
    
    public String creditosFinales(){
    	String salida = "";
    	salida += ("G R A C I A S   P O R   J U G A R\n");
    	salida += ("A L   B U S C A M I N A S\n");
    	salida += ("\n");
    	salida += ("C R E A D O   P O R   L A   C O M P A Ñ Í A\n");
    	salida += ("\tBurguete&Company™\n\n");
    	salida += ("-DESARROLLO:\n");
    	salida += ("\t+CODIFICACIÓN:\n");
    	salida += ("\t\t*Programación:\t\t\tDavid Burguete\n");
    	salida += ("\t\t*Diseño Gráfico:\t\t\tDavid Burguete\n");
        return salida;
    }
}
