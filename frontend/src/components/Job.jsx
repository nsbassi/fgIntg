const Job = (props) => {
  const { job, handleInputChange, startJob, saveLog } = props;
  return (
    <div className="col-sm-3 col-md-3">
      <div className="card pmd-card mb-4" style={{ marginRight: "10px" }}>
        <div className="card-header bg-primary pmd-card-dark d-flex flex-row">
          <div className="media-body">
            <h4 className="card-title">{job.label}</h4>
          </div>
        </div>
        <div className="card-body">
          <h5 className="card-subtitle">Last Sucessful Run</h5>
          <h6>{job.lastRun || "No Information found"}</h6>
          <h5 className="card-subtitle" style={{ marginTop: "5px" }}>
            Executed by
          </h5>
          <h6>{job.lastRunUser || "No Information found"}</h6>
          <h5 className="card-subtitle" style={{ marginTop: "5px" }}>
            Mode
          </h5>
          <h6 className="pmd-textfield">
            <select
              id="jobOption"
              data-jobname={job.type}
              className="form-control"
              value={job.option}
              name="jobOption"
              onChange={handleInputChange}
            >
              {job.options.map((o) => (
                <option key={o} value={o}>
                  {o}
                </option>
              ))}
            </select>
          </h6>

          <button
            data-jobname={job.type}
            className="btn btn-sm btn-primary pmd-btn-icon pmd-ripple-effect"
            type="button"
            disabled={job.running}
            onClick={startJob}
            title="Initiate Job"
            style={{ marginRight: "20px" }}
          >
            {(job.complete || !job.running) && (
              <i data-jobname={job.type} className="material-icons pmd-icon-xs">
                power_settings_new
              </i>
            )}
            Initiate
            {job.running && !job.complete && (
              <span
                className="spinner-border pmd-spinner spinner-border-sm text-light"
                role="status"
                aria-hidden="true"
              ></span>
            )}
          </button>
          <button
            data-jobname={job.type}
            className="btn btn-sm btn-primary pmd-btn-icon pmd-ripple-effect"
            type="button"
            disabled={job.running}
            onClick={startJob}
            title="Initiate Job"
          >
            {(job.complete || !job.running) && (
              <i data-jobname={job.type} className="material-icons pmd-icon-xs">
                download_for_offline
              </i>
            )}
            Get Logs
            {job.running && !job.complete && (
              <span
                className="spinner-border pmd-spinner spinner-border-sm text-light"
                role="status"
                aria-hidden="true"
              ></span>
            )}
          </button>
        </div>
      </div>
      {/* <div className="row pmd-textfield">
          <label
            htmlFor="jobOption"
            className="col-sm-4 col-form-label"
            style={{ textAlign: "right" }}
          >
            {job.label}
          </label>
          <div className="col-sm-3">
            <select
              id="jobOption"
              data-jobname={job.type}
              className="form-control"
              value={job.option}
              name="jobOption"
              onChange={handleInputChange}
            >
              {job.options.map((o) => (
                <option key={o} value={o}>
                  {o}
                </option>
              ))}
            </select>
          </div>
          <div className="col-sm-4">
            <button
              data-jobname={job.type}
              className="btn btn-sm btn-primary pmd-btn-fab pmd-btn-raised"
              type="button"
              disabled={job.running}
              onClick={startJob}
              title="Initiate Job"
            >
              {(job.complete || !job.running) && (
                <i
                  data-jobname={job.type}
                  className="material-icons pmd-icon-xs"
                >
                  power_settings_new
                </i>
              )}
              {job.running && !job.complete && (
                <span
                  className="spinner-border pmd-spinner spinner-border-sm text-light"
                  role="status"
                  aria-hidden="true"
                ></span>
              )}
            </button>

            {job.complete && job.status === "COMPLETED" && (
              <button
                data-jobname={job.type}
                style={{ marginLeft: "5px" }}
                className="btn btn-sm btn-success pmd-btn-fab pmd-btn-raised"
                type="button"
                onClick={saveLog}
                title="Download Logs"
              >
                <i
                  data-jobname={job.type}
                  className="material-icons pmd-icon-xs"
                >
                  check_circle_outline
                </i>
              </button>
            )}
            {job.complete && job.status !== "COMPLETED" && (
              <button
                data-jobname={job.type}
                style={{ marginLeft: "5px" }}
                className="btn btn-sm btn-danger pmd-btn-fab pmd-btn-raised"
                type="button"
                onClick={saveLog}
              >
                <i
                  data-jobname={job.type}
                  className="material-icons pmd-icon-xs"
                >
                  error_outline
                </i>
              </button>
            )}
          </div>
        </div> */}
    </div>
  );
};

export default Job;
