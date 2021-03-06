/**
 * ===================================================================================================================
 * =                   SCREEN SCREEN SCREEN SCREEN SCREEN SCREEN SCREEN SCREEN SCREEN SCREEN
 * =
 * =        +-------------------------------------------------------------------------------------> x
 * =        -
 * =        -   o   o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o
 * =        -   o   o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o
 * =        -   o   o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o
 * =        -   o   o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o
 * =        -   o   o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o
 * =        -   o   o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o
 * =        -   o   o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o
 * =        -   o   o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o
 * =        -   o   o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o
 * =        -   o   o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o
 * =        -   o   o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o
 * =        -   o   o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o
 * =        -   o   o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o
 * =        -   o   o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o  o
 * =        -
 * =        y
 * */

/**
 * @propFunctions: @onSeatClick
 * */
let Seat = React.createClass({
    getDefaultProps: function () {
        return {
            exists: true,
            x: 0,
            y: 0,
            width: 20,
            height: 30,
            margin: 10,
        }
    },
    render: function () {
        return (
            <span className="seat"
                  title={(this.props.y + 1) + ',' + (this.props.x + 1)}
                  style={{
                      position: 'absolute',
                      border: '2px solid black',
                      backgroundColor: this.props.exists ? 'white' : 'black',
                      left: ((this.props.width + this.props.margin) * (this.props.x + 1)) + 'px',
                      top: ((this.props.height + this.props.margin) * (this.props.y + 1)) + 'px',
                      width: this.props.width + 'px',
                      height: this.props.height + 'px',
                  }}
                  onClick={e => {
                      this.props.onSeatClick(this.props.x, this.props.y, this.props.exists);
                  }}
            >
            </span>
        );
    }
});

let Index = React.createClass({
    getDefaultProps: function () {
        return {
            x: 0,
            y: 0,
            i: 0,
            width: 20,
            height: 30,
            margin: 10,
        }
    },
    render: function () {
        return (
            <span className="seat pink white-text center "
                  style={{
                      position: 'absolute',
                      left: ((this.props.width + this.props.margin) * this.props.x) + 'px',
                      top: ((this.props.height + this.props.margin) * this.props.y) + 'px',
                      width: this.props.width + 'px',
                      height: this.props.height + 'px',
                  }}
            >
                {this.props.i}
            </span>
        );
    }
});

window.TheatreCreatorApp = React.createClass({
    getDefaultSeatMatrix: function (dimX, dimY) {
        let seats = [];
        for (let y = 0; y < dimY; y++) {
            seats.push([]);
            for (let x = 0; x < dimX; x++) {
                seats[y].push(true);
            }
        }
        return seats;
    },
    getInitialState: function () {
        return {
            seats: this.getDefaultSeatMatrix(20, 14),
        }
    },
    getDefaultProps: function () {
        return {
            submitUrl: '',
            contextPath: '',
            cinemaBuildingId: 0,
            MAX_X: 40,
            MAX_Y: 30,
            MIN_X: 4,
            MIN_Y: 4,
        }
    },
    isScreenNumberValid: function (screenNo) {
        if (isNaN(screenNo) || screenNo < 1) {
            Materialize.toast('Invalid Screen No', 1000);
            return false;
        }
        return true;
    },
    isDimensionValid: function (dimX, dimY) {
        if (isNaN(dimY) || isNaN(dimX)
            || dimX < 1 || dimX > this.props.MAX_X
            || dimY < 1 || dimY > this.props.MAX_Y
            || dimX < this.props.MIN_X || dimY < this.props.MIN_Y
        ) {
            Materialize.toast('Invalid Dimension', 1000);
            return false;
        }
        return true;
    },
    onDimensionChange: function () {
        let dimX = Number(this.refs.dimX.value);
        let dimY = Number(this.refs.dimY.value);
        if (this.isDimensionValid(dimX, dimY)) {
            this.setState({
                seats: this.getDefaultSeatMatrix(dimX, dimY),
            });
        }
    },
    toggleSeatExistence: function (x, y, doesExist) {
        this.setState(ps => {
            ps.seats[y][x] = !doesExist;
            return {seats: ps.seats}
        });
    },
    submit: function () {
        let createdSeats = [];
        let dimX = this.state.seats[0].length;
        let dimY = this.state.seats.length;
        for (let y = 0; y < dimY; y++) {
            for (let x = 0; x < dimX; x++) {
                let seat = this.state.seats[y][x];
                if (seat === true) {
                    createdSeats.push(x);
                    createdSeats.push(y);
                }
            }
        }
        let screenNo = this.refs.screenNo.value;
        if (!this.isScreenNumberValid(screenNo)) {
            return;
        }
        $(this.refs.screenNoFormField).val(screenNo);
        $(this.refs.seatsFormField).val(JSON.stringify(createdSeats));
        $(this.refs.form).submit();
    },
    render: function () {
        let dimX = this.state.seats[0].length;
        let dimY = this.state.seats.length;
        let indexedSeatsHTML = [];
        indexedSeatsHTML.push([]);
        indexedSeatsHTML[0].push('+');
        for (let x = 0; x < dimX; x++) {
            indexedSeatsHTML[0].push(
                <Index x={x + 1}
                       y={0}
                       i={x + 1}
                       key={x + 'x'}/>
            );
        }
        for (let y = 0; y < dimY; y++) {
            indexedSeatsHTML.push([]);
            indexedSeatsHTML[0].push(
                <Index x={0}
                       y={y + 1}
                       i={y + 1}
                       key={y + 'y'}/>
            );
            for (let x = 0; x < dimX; x++) {
                let seat = this.state.seats[y][x];
                indexedSeatsHTML[y].push(
                    <Seat x={x}
                          y={y}
                          key={x + '-' + y}
                          exists={seat}
                          onSeatClick={this.toggleSeatExistence}/>
                );
            }
        }
        return (
            <div>
                <form action={this.props.contextPath + this.props.submitUrl} method="POST" style={{display: 'none'}} ref="form">
                    <input type="number" name="screenNo" ref="screenNoFormField"/>
                    <input type="text" name="seats" ref="seatsFormField"/>
                    <input type="number" name="cinemaBuildingId" defaultValue={this.props.cinemaBuildingId}/>
                </form>
                <div className="row">
                    <div className="input-field col s3">
                        <input placeholder="Screen Number" type="number" ref="screenNo"/>
                    </div>
                    <div className="input-field col s3">
                        <input placeholder="rows" type="number" ref="dimY"
                               onChange={this.onDimensionChange}
                               defaultValue={dimY}/>
                    </div>
                    <div className="input-field col s3">
                        <input placeholder="columns" type="number" ref="dimX"
                               onChange={this.onDimensionChange}
                               defaultValue={dimX}/>
                    </div>
                    <div className="col s3">
                        <button className="btn btn-flat pink white-text" onClick={this.submit}>
                            Create
                        </button>
                    </div>
                </div>
                <div className="screen flow-text center-align grey white-text" style={{margin: '10px'}}>
                    SCREEN
                </div>
                <div id="seat-layout" style={{position: 'relative', margin: '10px'}}>
                    {indexedSeatsHTML}
                </div>
            </div>
        );
    }
});
