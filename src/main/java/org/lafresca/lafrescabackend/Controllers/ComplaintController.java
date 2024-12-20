package org.lafresca.lafrescabackend.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.lafresca.lafrescabackend.Models.Complaint;
import org.lafresca.lafrescabackend.Services.ComplaintService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/lafresca/complaint")
@AllArgsConstructor
@Tag(name = "Complaint Controller")
public class ComplaintController {
    private final ComplaintService complaintService;

    // Add new Complaint
    @PostMapping
    @Operation(
            description = "Add to complaint",
            summary = "Add new complaint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            })
    public String addNewComplaint(@Valid @RequestBody Complaint complaint) { return complaintService.addNewComplaint(complaint); }


    //Get All complaints
    @GetMapping
    @Operation(
            description = "Get all Complaints",
            summary = "Retrieve all complaints",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            })

    public List<Complaint> getComplaints() {
        return complaintService.getComplaints(); }

    //Delete Complaints
    @DeleteMapping(path = "/{complaintId}")
    @Operation(
            description = "Delete complaint",
            summary = "Delete complaint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            })
    public String deleteComplaint(@PathVariable("complaintId") String id) {
         return complaintService.deleteComplaint(id);
    }

    //find All by complainer Id
    @GetMapping(path = "findComplainByComplainer/{complainerId}")
    @Operation(
            description = "Search by complainerId",
            summary = "GetComplaints By Complainer",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            })
    public List<Complaint> GetComplaintsByComplainer(@PathVariable("complainerId") String id) {
        return complaintService.GetComplaintsByComplainer(id);
    }

    //Find All by comlainee Id
    @GetMapping(path = "findComplainByComplainee/{complaineeId}")
    @Operation(
            description = "Search by complaineeId",
            summary = "Get Complaints By Complainee",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            })
    public List<Complaint> GetComplaintsByComplainee(@PathVariable("complaineeId") String id) {
        return complaintService.GetComplaintsByComplainee(id);
    }



}
