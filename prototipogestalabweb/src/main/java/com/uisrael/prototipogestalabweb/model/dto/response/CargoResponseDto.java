package com.uisrael.prototipogestalabweb.model.dto.response;

public class CargoResponseDto {
	
	private int idCargo;
	private String nombre;
	private String descripcion;
	private boolean estadoCargo;
	public int getIdCargo() {
		return idCargo;
	}
	public void setIdCargo(int idCargo) {
		this.idCargo = idCargo;
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
	public boolean isEstadoCargo() {
		return estadoCargo;
	}
	public void setEstadoCargo(boolean estadoCargo) {
		this.estadoCargo = estadoCargo;
	}

}
