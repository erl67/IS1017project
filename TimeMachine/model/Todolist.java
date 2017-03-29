package model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the todolist database table.
 * 
 */
@Entity
@NamedQuery(name="Todolist.findAll", query="SELECT t FROM Todolist t")
public class Todolist implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String description;

	private Timestamp timestamp;

	//bi-directional many-to-one association to Todolist
	@ManyToOne
	@JoinColumn(name="fk_id")
	private Todolist todolist;

	//bi-directional many-to-one association to Todolist
	@OneToMany(mappedBy="todolist")
	private List<Todolist> todolists;

	public Todolist() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Todolist getTodolist() {
		return this.todolist;
	}

	public void setTodolist(Todolist todolist) {
		this.todolist = todolist;
	}

	public List<Todolist> getTodolists() {
		return this.todolists;
	}

	public void setTodolists(List<Todolist> todolists) {
		this.todolists = todolists;
	}

	public Todolist addTodolist(Todolist todolist) {
		getTodolists().add(todolist);
		todolist.setTodolist(this);

		return todolist;
	}

	public Todolist removeTodolist(Todolist todolist) {
		getTodolists().remove(todolist);
		todolist.setTodolist(null);

		return todolist;
	}

}