import {Component, OnInit} from '@angular/core';
import {Employee} from "../_models/employee";
import {EmployeeService} from "../_services/employee.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {NgForm} from "@angular/forms";
import {Meeting} from "../_models/meeting";
import {MeetingService} from "../_services/meeting.service";
import {Response} from "../_models/response";
import {BusyTimeService} from "../_services/busy-time.service";
import {BusyTime} from "../_models/busyTimes";

@Component({
  selector: 'app-meeting-create',
  templateUrl: './meeting-create.component.html',
  styleUrls: ['./meeting-create.component.css']
})
export class MeetingCreateComponent implements OnInit {

  model:any = {}
  meeting: Meeting = {} as Meeting;
  employees: Employee[] = [];
  participantIds: number[] = [];
  filteredEmployees: Employee[] = [];
  filteredFirst: boolean = false;
  minStartDate: Date = new Date();
  minEndDate: Date = new Date();
  startDate: Date = new Date();
  endDate: Date = new Date();
  startDateSelected: boolean = false;
  endDateSelected: boolean = false;
  addedEmployees: Employee[] = [];
  employeesAdded: boolean = false;
  meetings: Meeting[] = [];
  busyTimes: BusyTime[] = [];
  meetings_aux: Meeting[] = [];
  busyTimes_aux: BusyTime[] = [];
  meeting_found: boolean = false;

  constructor(private meetingService: MeetingService, private busyTimeService: BusyTimeService, private employeeService: EmployeeService, private router: Router, private toastr: ToastrService) { }

  ngOnInit(): void {
    this.getAllEnabledEmployees();
  }

  create(model: NgForm) {
    if(this.validateDates(this.model.startDate, this.model.endDate)) {
      this.model.startDate = new Date(this.model.startDate);
      this.model.endDate = new Date(this.model.endDate);

      this.participantIds = [];
      this.addedEmployees.forEach(employee => {this.participantIds.push(employee.id)});
      this.model.participantIds = this.participantIds;

      this.meetingService.create(this.model).subscribe({
        next: (response: Meeting) => {
          this.router.navigateByUrl('/meetings-list');
        },
        error: err => {
          this.startDateSelected = false;
          this.endDateSelected = false;
          this.filteredFirst = false;
          this.toastr.error(err.error);
          this.router.navigateByUrl('/meeting-create');
        },
        complete: () => {
          this.toastr.success("Meeting created!");
        }
      })
    }
  }

  validateDates(startDate: Date, endDate: Date) {
    this.minStartDate = new Date();
    this.minEndDate = new Date();
    this.minStartDate.setMinutes(this.minStartDate.getMinutes() + 15);
    this.minEndDate.setMinutes(this.startDate.getMinutes() + 15);
    startDate = new Date(startDate);
    endDate = new Date(endDate);
    if(startDate >= this.minStartDate) {
      if((endDate >= this.minEndDate) && (startDate.getFullYear() === endDate.getFullYear()) && (startDate.getMonth() === endDate.getMonth()) && (startDate.getDate() === endDate.getDate())){
        return true;
      } else {
        this.toastr.error("End date invalid!");
        return false;
      }
    } else {
      this.toastr.error("Start date invalid!");
      return false;
    }
  }

  selectStartDate(date: Date) {
    date = new Date(date);
    this.minStartDate = new Date();
    this.minStartDate.setMinutes(this.minStartDate.getMinutes() + 15);
    if(date >= this.minStartDate) {
      this.startDate = date;
      this.startDateSelected = true;
      this.toastr.info("Start date selected!");
    } else {
      this.startDateSelected = false;
      this.endDate = {} as Date;
      this.toastr.error("Start date invalid!")
    }
    this.minEndDate = new Date(date);
    this.minEndDate.setMinutes(date.getMinutes() + 15);
  }

  selectEndDate(date: Date) {
    date = new Date(date);
    this.minEndDate = new Date(this.startDate);
    this.minEndDate.setMinutes(this.startDate.getMinutes() + 15);
    if((date >= this.minEndDate) && (this.startDate.getFullYear() === date.getFullYear()) && (this.startDate.getMonth() === date.getMonth()) && (this.startDate.getDate() === date.getDate())) {
      this.endDate = date;
      this.endDateSelected = true;
      this.toastr.info("End date selected!");
    } else {
      this.endDateSelected = false;
      this.toastr.error("End date invalid!")
    }
  }

  getAllEnabledEmployees() {
    this.employeeService.getAllEnabled().subscribe({
      next: (response: Employee[]) => {
        this.employees = response;
        this.filteredEmployees = this.employees;
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

  filterResults(text: string) {
    this.filteredFirst = true;
    if (!text) {
      this.filteredEmployees = this.employees;
    }
    this.filteredEmployees = this.employees.filter(employee => employee?.userName.toLowerCase().includes(text.toLowerCase()) || employee?.firstName.toLowerCase().includes(text.toLowerCase()));
  }

  addEmployee(employee: Employee) {
    if(this.addedEmployees.find(e => e.id === employee.id)) {
      this.toastr.info("Employee already added!");
    } else {
      this.addedEmployees.push(employee);
      this.employeesAdded = true;
      this.toastr.info("Employee added!");
      this.setMeetings();
    }
  }

  filter(employee: Employee, employees: Employee[]) {
    let employeesFiltered: Employee[] = [];
    employees.forEach(e => {
      if(e.id !== employee.id) {
        employeesFiltered.push(e);
      }
    })
    return employeesFiltered;
  }

  removeEmployee(employee: Employee) {
    if(this.addedEmployees.find(e => e.id === employee.id)) {
      // this.addedEmployees.filter(e => e.id != employee.id)
      this.addedEmployees = this.filter(employee, this.addedEmployees);
      this.toastr.info("Employee removed!");
      this.setMeetings();
    } else {
      this.toastr.info("Employee not found!")
    }
    if(this.addedEmployees.length === 0) {
      this.employeesAdded = false;
      this.toastr.info("No employees set for this meeting!");
    }
  }

  setMeetings() {
    this.meetings = [];
    this.busyTimes = [];
    this.addedEmployees.forEach(e => {
      this.getByDateAndResponseAndEmployee(this.startDate, Response.YES, e.id);
      this.getBusyTimesByDateAndEmployee(this.startDate, e.id);
    })
  }

  getByDateAndResponseAndEmployee(date: Date, response: Response, employeeId: number) {
    this.meetingService.getByDateAndResponseAndEmployee(date, response, employeeId).subscribe({
      next: (response: Meeting[]) => {
        this.meetings_aux = response;
        // this.meetings = this.meetings.concat(this.meetings_aux);
        this.meetings_aux.forEach(m => {
          this.meeting_found = false;
          this.meetings.forEach(m2 => {
            if(m2.id === m.id) {
              this.meeting_found = true;
            }
          })
          if(!this.meeting_found) {
            this.meetings.push(m);
          }
        })
      },
      error: err => {
        console.log("error = ", err.error);
        this.toastr.error(err.error);
      }
    })
  }

  getBusyTimesByDateAndEmployee(date: Date, employeeId: number) {
    this.busyTimeService.getByDateAndEmployee(date, employeeId).subscribe({
      next: (response: BusyTime[]) => {
        this.busyTimes_aux = response;
        this.busyTimes = this.busyTimes.concat(this.busyTimes_aux);
      },
      error: err => {
        console.log("error = ", err.error);
        this.toastr.error(err.error);
      }
    })
  }

}
