import "./main.css";
import React, { Component } from "react";
import "react-toastify/dist/ReactToastify.css";
import Axios from "axios";
import queryString from "query-string";
import Job from "./components/Job";
import TblHdr from "./components/TblHdr";
import NavBar from "./components/NavBar";
import MsgBox from "./components/MsgBox";
import HdrRow from "./components/HdrRow";
import { toast } from "react-toastify";

class App extends Component {
  state = {
    jobs: [],
    instances: ["FGP", "OSP"],
    instance: "FGP",
  };

  intervalId = {};

  constructor(props) {
    super(props);
    const value = queryString.parse(window.location.search);
    const token = value.token;
    Axios.defaults.headers.common["X-Authorization"] = token;
    Axios.defaults.baseURL = process.env.REACT_APP_BACKEND_URL;
  }

  async componentDidMount() {
    this.loadJobs(this.state.instance);
  }

  async loadJobs(instance) {
    try {
      const response = await Axios.get("jobs", {
        params: {
          instance,
        },
      });
      if (response.status === 200) {
        const jobs = response.data;
        this.setState({ jobs });
      }
    } catch (e) {
      this.notify([
        {
          idx: 1,
          text: `Failed to load job definitions. ${e.message}`,
        },
      ]);
    }
  }

  notify = (errors) =>
    toast.error(
      <ul className="list-group list-group-flush">
        {errors.map((e) => (
          <li
            key={e.idx}
            className="list-group-item"
            style={{
              backgroundColor: "#e74c3c",
            }}>
            {e.text}
          </li>
        ))}
      </ul>
    );

  saveLog = async () => {
    // const { job } = this.state;
    // const { data } = await Axios({
    //   url: `/bulkedit/jobs/download/${job.id}`,
    //   method: "get",
    //   headers: { "content-type": "application/octet-stream" },
    //   responseType: "blob",
    // });
    // fileDownload(data, `${job.number}.xlsx`);
  };

  handleInputChange = (e) => {
    const jobType = e.target.getAttribute("data-jobname");
    const val = e.target.value;
    const { jobs } = this.state;
    let job = jobs.find((j) => j.type === jobType);
    job.option = val;
    this.setState({ jobs });
  };

  reloadStatus = async (job) => {
    const { jobs } = this.state;

    if (job.parentId && job.running) {
      const { data } = await Axios.post("/status", job);

      if (data.parentId && data.running) {
        this.intervalId[job.type] = setTimeout(
          this.reloadStatus.bind(this, data),
          10000
        );
      } else {
        let ojob = jobs.find((j) => j.type === job.type);
        Object.assign(ojob, data);
        this.setState({ jobs });
        this.notify([
          {
            idx: 1,
            text:
              ojob.label +
              " import job initiated successfully. You can track it's staus using id " +
              ojob.essJobId,
          },
        ]);
        clearTimeout(this.intervalId[job.type]);
      }
    } else {
      let ojob = jobs.find((j) => j.type === job.type);
      ojob.complete = true;
      ojob.running = false;
      this.setState({ jobs });
      if (ojob.essJobId)
        this.notify([
          {
            idx: 1,
            text:
              ojob.label +
              " import job initiated successfully. You can track it's staus using id " +
              ojob.essJobId,
          },
        ]);
      clearTimeout(this.intervalId[job.type]);
    }
  };

  insChg = (e) => {
    const instance = e.target.getAttribute("data-instance");
    this.setState({ instance });
    this.loadJobs(instance);
  };

  startJob = async (e) => {
    const jobType = e.target.getAttribute("data-jobname");
    const { jobs } = this.state;
    let job = jobs.find((j) => j.type === jobType);
    Object.assign(job, {
      parentId: null,
      essJobId: null,
      status: null,
      link: null,
      running: true,
      complete: false,
    });
    this.setState({ jobs });

    try {
      job.instance = this.state.instance;
      const { data } = await Axios.post("initiate", job);
      Object.assign(job, data);
      this.setState({ jobs });
      this.intervalId[job.type] = setTimeout(
        this.reloadStatus.bind(this, job),
        10000
      );
    } catch (e) {
      this.notify([
        {
          idx: 1,
          text: "Failed to intiate import for " + job.label + ". " + e.message,
        },
      ]);

      Object.assign(job, {
        status: "failed",
        link: null,
        running: false,
        complete: false,
      });
      this.setState({ jobs });
    }
  };

  render() {
    const { instance, instances, jobs } = this.state;
    return (
      <div style={{ maxWidth: "1100px", margin: "20px auto" }}>
        <NavBar
          instances={instances}
          instance={instance}
          insChg={this.insChg}
        />
        <TblHdr jobs={jobs} handleOpenModal={this.handleOpenModal} />
        <div className="table-responsive" style={{ maxHeight: "500px" }}>
          <table className="table table-hover">
            <HdrRow />
            <tbody>
              {jobs.map((job) => {
                return (
                  <Job
                    key={job.type}
                    job={job}
                    handleInputChange={this.handleInputChange}
                    startJob={this.startJob}
                    saveLog={this.saveLog}
                  />
                );
              })}
            </tbody>
          </table>
        </div>
        <MsgBox notify={this.notify} />
      </div>
    );
  }
}

export default App;
