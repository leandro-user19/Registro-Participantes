package org.example.registroparticipantes;

public class Participante {
    private int id;
    private String cedula;
    private String nombre;
    private String apellido;
    private int edad;
    private String correo;
    private String estadoCivil;
    private String jornada;
    private String categoria;
    private String observaciones;

    // Constructor completo
    public Participante(int id, String cedula, String nombre, String apellido, int edad,
                        String correo, String estadoCivil, String jornada, String categoria, String observaciones) {
        this.id = id;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.correo = correo;
        this.estadoCivil = estadoCivil;
        this.jornada = jornada;
        this.categoria = categoria;
        this.observaciones = observaciones;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(String estadoCivil) { this.estadoCivil = estadoCivil; }

    public String getJornada() { return jornada; }
    public void setJornada(String jornada) { this.jornada = jornada; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getObservaciones() { return observaciones; }
    public void setObservations(String observaciones) { this.observaciones = observaciones; }
}