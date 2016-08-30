package model.jsonClass;

public class EstadisticasBatalla {

    public long id;
    public String tipo;
    public String resultado;
    public String batalla;
    public String muertes = "";
    public String calculos = "";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getBatalla() {
        return batalla;
    }

    public void setBatalla(String batalla) {
        this.batalla = batalla;
    }

    public String getMuertes() {
        return muertes;
    }

    public void setMuertes(String muertes) {
        this.muertes = muertes;
    }

    public String getCalculos() {
        return calculos;
    }

    public void setCalculos(String calculos) {
        this.calculos = calculos;
    }
}
