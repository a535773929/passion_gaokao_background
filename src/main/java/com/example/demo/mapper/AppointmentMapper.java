package com.example.demo.mapper;

import com.example.demo.entity.Appointment;
import com.example.demo.entity.BindNumber;
import com.example.demo.entity.Expert;
import com.example.demo.entity.Student;
import org.apache.ibatis.annotations.*;

import java.sql.Date;
import java.util.List;

@Mapper
public interface AppointmentMapper {
    @Select("select distinct * from (gaokao_appointment left join (select buy_time,appointment_id from  gaokao_order) as temp_order on gaokao_appointment.appointment_id=temp_order.appointment_id left join (select student_id,student_name from  gaokao_student) as student on gaokao_appointment.student_id=student.student_id left join (select group_id,group_name from gaokao_expert_group) as group_list on gaokao_appointment.group_id=group_list.group_id left join (select expert_id,expert_name from gaokao_expert) as expert_list on gaokao_appointment.expert_id=expert_list.expert_id) where appointment_time>=#{today} or appointment_time2>=#{today} or status=1 or status=2 or status2=1 or status2=2")
    List<Appointment> findCurrentAppointment(String today);

    @Select("select distinct * from (gaokao_appointment left join (select buy_time,appointment_id from  gaokao_order) as temp_order on gaokao_appointment.appointment_id=temp_order.appointment_id left join (select student_id,student_name from  gaokao_student) as student on gaokao_appointment.student_id=student.student_id left join (select group_id,group_name from gaokao_expert_group) as group_list on gaokao_appointment.group_id=group_list.group_id left join (select expert_id,expert_name from gaokao_expert) as expert_list on gaokao_appointment.expert_id=expert_list.expert_id)")
    List<Appointment> findAll();

    @Select("select student_name,gender,phone,address,student_type,total_score from gaokao_student where student_id=#{id}")
    Student findStudentInfo(@Param("id") int id);

    @Select("select expert_id from gaokao_expert_group where group_id=#{id}")
    String findByGroup(@Param("id") int id);

    @Select("<script>"
            +"select * from gaokao_expert where expert_id in "
            + "<foreach item='item' index='index' collection='expertsId' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>"
            )
    List<Expert> findExperts(@Param("expertsId") List<String> expertsId);

    @Update("update gaokao_appointment set expert_id=#{expert_id} where appointment_id=#{appointment_id}")
    int setExpert(@Param("appointment_id") int appointment_id,@Param("expert_id") int expert_id);

    @Select("select phone from gaokao_expert where expert_id=#{id}")
    String findExpert(@Param("id") int id);


}
