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
    @Select("select * from (gaokao_appointment left join (select appointment_id,buy_time from gaokao_order) as temp_order on gaokao_appointment.appointment_id=temp_order.appointment_id left join (select student_id,student_name from  gaokao_student) as student on gaokao_appointment.student_id=student.student_id left join (select group_id,group_name from gaokao_expert_group) as group_list on gaokao_appointment.group_id=group_list.group_id left join (select expert_id,expert_name from gaokao_expert) as expert_list on gaokao_appointment.expert_id=expert_list.expert_id) where buy_time>=#{today} or status=1 or status=2")
    List<Appointment> findAll(String today);

    @Update("update gaokao_appointment set status=#{status} where appointment_id=#{id}")
    int confirm(@Param("id") int id,@Param("status") int status);

    @Select("select student_name,gender,phone,address,student_type,total_score from gaokao_student where student_id=#{id}")
    Student findStudentInfo(@Param("id") int id);

    @Select("select expert_id,expert_name,phone from gaokao_expert where group_id=#{id}")
    List<Expert> findByGroup(@Param("id") int id);

    @Update("update gaokao_appointment set expert_id=#{expert_id} where appointment_id=#{appointment_id}")
    int setExpert(@Param("appointment_id") int appointment_id,@Param("expert_id") int expert_id);

    @Select("select phone from gaokao_expert where expert_id=#{id}")
    String findExpert(@Param("id") int id);


}
