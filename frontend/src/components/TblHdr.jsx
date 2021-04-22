const TblHdr = (props) => {
  const { jobs } = props;

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-grey">
      <div className="btn-group">
        <button
          id="btnFilter"
          className="btn pmd-btn-flat pmd-ripple-effect btn-grey"
          type="button">
          <i className="material-icons pmd-md">settings_input_component</i>
        </button>

        <button className="btn pmd-ripple-effect btn-grey" type="button">
          <strong>
            {jobs.length + " out of " + jobs.length + " elements"}
          </strong>
        </button>
      </div>
      <div
        className="collapse navbar-collapse"
        id="navbarSupportedContent"></div>
      <div className="btn-group">
        <button className="btn btn-grey" type="button">
          <i className="material-icons">refresh</i>
        </button>
        <button className="btn btn-grey" type="button">
          <i className="material-icons">view_list</i>
        </button>
        <button className="btn btn-grey" type="button">
          <i className="material-icons pmd-sm">widgets</i>
        </button>
      </div>
    </nav>
  );
};

export default TblHdr;
