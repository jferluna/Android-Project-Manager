/*
Project Manager - Android application for the administration of projects.
	Copyright (C) 2014 - ITESM

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.


Authors:

   ITESM representatives
	Ing. Martha Sordia Salinas <msordia@itesm.mx>
    Ing. Mario de la Fuente <mario.delafuente@itesm.mx>

   ITESM students
	David Alberto De Leon Villarreal ddeleon93@gmail.com
	Alan Salinas Gonzalez alan.sagz@gmail
	José Fernando Luna Alemán jfernando.luna91@gmail.com
*/

package com.vaquerosisd.object;

public class Project {
	private int id;
	private String name;
	private int yearStartDate;
	private int monthStartDate;
	private int dayStartDate;
	private int yearDueDate;
	private int monthDueDate;
	private int dayDueDate;
	private String status;
	private int openTasks;
	private int totalTasks;
	private String contentPath;
	
	public Project() {
	}

	public Project(int id, String name, int yearStartDate, int monthStartDate, int dayStartDate, int yearDueDate, 
			int monthDueDate, int dyaDueDate, String status, int openTasks, int totalTasks,	String contentPath) {
		super();
		this.id = id;
		this.name = name;
		this.yearStartDate = yearStartDate;
		this.monthStartDate = monthStartDate;
		this.dayStartDate = dayStartDate;
		this.yearDueDate = yearDueDate;
		this.monthDueDate = monthDueDate;
		this.dayDueDate = dyaDueDate;
		this.status = status;
		this.openTasks = openTasks;
		this.totalTasks = totalTasks;
		this.contentPath = contentPath;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getYearStartDate() {
		return yearStartDate;
	}

	public void setYearStartDate(int yearStartDate) {
		this.yearStartDate = yearStartDate;
	}

	public int getMonthStartDate() {
		return monthStartDate;
	}

	public void setMonthStartDate(int monthStartDate) {
		this.monthStartDate = monthStartDate;
	}

	public int getDayStartDate() {
		return dayStartDate;
	}

	public void setDayStartDate(int dayStartDate) {
		this.dayStartDate = dayStartDate;
	}

	public int getYearDueDate() {
		return yearDueDate;
	}

	public void setYearDueDate(int yearDueDate) {
		this.yearDueDate = yearDueDate;
	}

	public int getMonthDueDate() {
		return monthDueDate;
	}

	public void setMonthDueDate(int monthDueDate) {
		this.monthDueDate = monthDueDate;
	}

	public int getDayDueDate() {
		return dayDueDate;
	}

	public void setDayDueDate(int dayDueDate) {
		this.dayDueDate = dayDueDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getOpenTasks() {
		return openTasks;
	}

	public void setOpenTasks(int openTasks) {
		this.openTasks = openTasks;
	}

	public int getTotalTasks() {
		return totalTasks;
	}

	public void setTotalTasks(int totalTasks) {
		this.totalTasks = totalTasks;
	}

	public String getContentPath() {
		return contentPath;
	}

	public void setContentPath(String contentPath) {
		this.contentPath = contentPath;
	}
	
	
	
}

