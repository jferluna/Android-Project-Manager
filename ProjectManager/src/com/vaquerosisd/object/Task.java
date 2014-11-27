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

public class Task {
	private int taskId;
	private int projectId;
	private String taskName;
	private String status;
	private String priority;
	private int percentage;
	private int yearStartDate;
	private int monthStartDate;
	private int dayStartDate;
	private int yearDueDate;
	private int monthDueDate;
	private int dayDueDate;
	private String photoPath;
	private String description;
	
	public Task() {
		
	}

	public Task(int taskId, int projectId, String taskName, String status,
			String priority, int percentage, int yearStartDate,
			int monthStartDate, int dayStartDate, int yearDueDate,
			int monthDueDate, int dayDueDate, String photoPath,
			String description) {
		super();
		this.taskId = taskId;
		this.projectId = projectId;
		this.taskName = taskName;
		this.status = status;
		this.priority = priority;
		this.percentage = percentage;
		this.yearStartDate = yearStartDate;
		this.monthStartDate = monthStartDate;
		this.dayStartDate = dayStartDate;
		this.yearDueDate = yearDueDate;
		this.monthDueDate = monthDueDate;
		this.dayDueDate = dayDueDate;
		this.photoPath = photoPath;
		this.description = description;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
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

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
