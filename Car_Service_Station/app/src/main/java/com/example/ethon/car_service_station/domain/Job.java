package com.example.ethon.car_service_station.domain;

import java.io.Serializable;

/**
 * Created by Ethon on 2016/10/31.
 */

public class Job implements Serializable {

    private Long id;
    private String jobDate;
    private String description;


    public Job() {
    }

    public Job(Builder builder) {
        id=builder.id;
        jobDate=builder.jobDate;
        description=builder.description;

    }

    public static class Builder{
        private Long id;
        private String jobDate;
        private String description;


        public Builder(String jobDate)
        {
            this.jobDate=jobDate;
        }

        public Builder id(Long id)
        {
            this.id=id;
            return this;
        }

        public Builder description(String desc)
        {
            this.description=desc;
            return this;
        }


        public Builder copy(Job job)
        {
            this.description=job.description;
            this.id=job.id;

            return this;
        }

        public Job build()
        {
            return new Job(this);
        }
    }

    public Long getId() {
        return id;
    }

    public String getJobDate() {
        return jobDate;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Job job = (Job) o;

        if (id != null ? !id.equals(job.id) : job.id != null) return false;
        if (jobDate != null ? !jobDate.equals(job.jobDate) : job.jobDate != null) return false;
        return description != null ? description.equals(job.description) : job.description == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (jobDate != null ? jobDate.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", jobDate='" + jobDate + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
