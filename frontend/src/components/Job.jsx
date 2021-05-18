const Job = (props) => {
  const { job, handleInputChange, startJob, saveLog } = props;
  return (
    <tr key={job.type}>
      <td>
        <i className="material-icons pmd-md">{job.icon}</i>
      </td>
      <td>{job.label}</td>
      <td>{job.group}</td>
      <td>{job.lastRunDate}</td>
      <td>{job.runBy}</td>
      <td>
        <i
          className={
            "material-icons pmd-md status-" + (job.status || "unknown")
          }>
          {job.status === "success"
            ? "check_circle"
            : job.status === "failed" || job.status === "ERROR" || job.status === "FAILED" || job.status === "WARNING" || job.status === "CANCELLED"
            ? "error"
            : "remove_circle"}
        </i>
      </td>
      <td>
        <button
          data-jobname={job.type}
          enabled={"" + job.hasLogs}
          className={
            "mr-4 btn btn-xs pmd-btn-fab pmd-ripple-effect pmd-btn-outline btn-grey"
          }
          onClick={saveLog}
          data-tip="Download Logs">
          <i className="material-icons pmd-icon-xs" data-jobname={job.type}>
            file_download
          </i>
        </button>
      </td>
      <td>
        <select
          id="jobOption"
          data-jobname={job.type}
          className="form-control form-control-sm"
          value={job.option}
          name="jobOption"
          onChange={handleInputChange}>
          {job.options.map((o) => (
            <option key={o} value={o}>
              {o}
            </option>
          ))}
        </select>
      </td>
      <td>
        <button
          data-jobname={job.type}
          className="btn btn-xs pmd-btn-fab pmd-ripple-effect pmd-btn-outline btn-primary"
          type="button"
          disabled={job.running}
          onClick={startJob}
          title="Initiate Job">
          {!job.running && (
            <i data-jobname={job.type} className="material-icons pmd-icon">
              power_settings_new
            </i>
          )}
          {job.running && (
            <span
              className="spinner-border pmd-spinner spinner-border-sm text-primary"
              role="status"
              aria-hidden="true"></span>
          )}
        </button>
      </td>
    </tr>
  );
};

export default Job;
