package pt.cosn.mascontroller.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class WorkflowExecution {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String workflowId;

  @Column(nullable = false)
  private String domainModelId;

  @Column(nullable = false)
  private String userId;

  @Column
  private String description;

  @Column
  private int steps;

  @Column
  private int currentStep;

}
