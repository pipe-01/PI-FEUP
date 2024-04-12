package pt.cosn.mascontroller.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Log {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  private WorkflowExecution workflowExecution;

  @Column(nullable = false)
  private String action;

  @Column
  private String message;

}
