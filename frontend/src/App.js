import "./App.css";
import React, { Component } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Axios from "axios";
import queryString from "query-string";
import fileDownload from "js-file-download";
import { joblist } from "./components/jobs";
import Job from "./components/Job";

class App extends Component {
  state = {
    jobs: joblist,
  };
  intervalId = {};

  constructor(props) {
    super(props);
    const value = queryString.parse(window.location.search);
    const token = value.token;
    Axios.defaults.headers.common["X-Authorization"] = token;
    Axios.defaults.baseURL = process.env.REACT_APP_BACKEND_URL;
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
            }}
          >
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

  handleInputChange = (e) => {
    const jobType = e.target.getAttribute("data-jobname");
    const val = e.target.value;
    const { jobs } = this.state;
    let job = jobs.find((j) => j.type === jobType);
    job.option = val;
    this.setState({ jobs });
  };

  startJob = async (e) => {
    const jobType = e.target.getAttribute("data-jobname");
    const { jobs } = this.state;
    let job = jobs.find((j) => j.type === jobType);
    Object.assign(job, {
      id: null,
      parentId: null,
      status: null,
      link: null,
      running: true,
      complete: false,
    });
    this.setState({ jobs });

    try {
      const { data } = await Axios.post("/initiate", job);
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
        id: null,
        parentId: null,
        status: "FAILED",
        link: null,
        running: false,
        complete: false,
      });
      this.setState({ jobs });
    }
  };

  render() {
    const { instance, jobs } = this.state;
    return (
      <div className="container-fluid">
        <div className="row" style={{ margin: "0 20px" }}>
          <div className="col-sm-12">
            <div className="row pmd-textfield" style={{ marginTop: "10px" }}>
              <label
                htmlFor="qtyToAdd"
                className="offset-sm-8 col-sm-1 col-form-label"
              >
                <strong>Instance</strong>
              </label>
              <div className="col-sm-1">
                <select
                  id="propeller-select"
                  className="form-control"
                  value={instance}
                  name="action"
                  onChange={this.handleInputChange}
                >
                  <option value="FGP">FGP</option>
                </select>
              </div>
            </div>
          </div>
        </div>
        <div className="row" style={{ margin: "0 20px" }}>
          <div className="col-sm-12">
            <h3 style={{ margin: "0" }}>Master Data</h3>
          </div>
        </div>
        <hr style={{ margin: "0 0 15px 0" }} />
        <div className="row" style={{ margin: "0 20px" }}>
          {jobs
            .filter((j) => j.group === "master")
            .map((job) => (
              <Job
                key={job.type}
                job={job}
                handleInputChange={this.handleInputChange}
                startJob={this.startJob}
                saveLog={this.saveLog}
              />
            ))}
        </div>
        <div className="row" style={{ marginTop: "20px" }}>
          <div className="col-sm-12">
            <h3 style={{ margin: "0" }}>Transactional Data</h3>
          </div>
        </div>
        <hr style={{ margin: "0 0 15px 0" }} />
        <div className="row" style={{ margin: "0 20px" }}>
          {jobs
            .filter((j) => j.group === "transactional")
            .map((job) => (
              <Job
                key={job.type}
                job={job}
                handleInputChange={this.handleInputChange}
                startJob={this.startJob}
                saveLog={this.saveLog}
              />
            ))}
        </div>
        <ToastContainer
          position="top-right"
          autoClose={5000}
          hideProgressBar
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
        />
      </div>
    );
  }
}

export default App;
