package com.example.demo.mapper;

import com.example.demo.entity.Appointment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Date;
import java.util.List;

@Mapper
public interface AppointmentMapper {
    @Select("select * from (gaokao_appointment left join (select appointment_id,buy_time from gaokao_order) as temp_order on gaokao_appointment.appointment_id=temp_order.appointment_id left join (select student_id,student_name from  gaokao_student) as student on gaokao_appointment.student_id=student.student_id left join (select group_id,group_name from gaokao_expert_group) as group_list on gaokao_appointment.group_id=group_list.group_id left join (select expert_id,expert_name from gaokao_expert) as expert_list on gaokao_appointment.expert_id=expert_list.expert_id) where buy_time>=#{today} or status=0")
    List<Appointment> findAll(String today);
    @Update("update gaokao_appointment set status=#{confirmType} where appointment_id=#{id}")
    int confirm(int id,int confirmType);
}
