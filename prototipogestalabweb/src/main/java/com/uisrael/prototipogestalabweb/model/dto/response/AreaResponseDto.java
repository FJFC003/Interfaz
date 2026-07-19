package com.uisrael.prototipogestalabweb.model.dto.response;

public class AreaResponseDto {
	
	private int idArea;
	private String nombre;
	private String descripcion;
	private boolean estadoArea;
	public int getIdArea() {
		return idArea;
	}
	public void setIdArea(int idArea) {
		this.idArea = idArea;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public boolean isEstadoArea() {
		return estadoArea;
	}
	public void setEstadoArea(boolean estadoArea) {
		this.estadoArea = estadoArea;
	}

}
