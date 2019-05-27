package com.gizwits.lease.event;

import org.springframework.context.ApplicationEvent;

public class NameModifyEvent<E> extends ApplicationEvent {

	private static final long serialVersionUID = 3886029444529074805L;

	private E id;
	private String oldName;
	private String newName;

	public NameModifyEvent(Object source) {
		super(source);
	}

	public NameModifyEvent(Object source, E id, String oldName, String newName) {
		super(source);
		this.id = id;
		this.oldName = oldName;
		this.newName = newName;
	}

	public E getId() {
		return id;
	}

	public void setId(E id) {
		this.id = id;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}
}
