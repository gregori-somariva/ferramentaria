package com.mademil.ferramentaria.controllers;

import com.mademil.ferramentaria.service.*;
import com.mademil.ferramentaria.entities.*;
import com.mademil.ferramentaria.enums.*;
import com.mademil.ferramentaria.dto.CompleteFormDataDTO;
import com.mademil.ferramentaria.dto.FormSubmissionToolDTO;
import com.mademil.ferramentaria.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;

@Controller
public class ProcessSheetController {
    
    @Autowired
    ChuckService chuckService;

    @Autowired
    MachineService machineService;

    @Autowired
    FormSubmissionService formSubmissionService;

    @Autowired
    FormSubmissionToolService formSubmissionToolService;

    @Autowired
    OperationService operationService;

    @Autowired
    ToolService toolService;

    @Autowired
    HtmlDocumentService htmlDocumentService;

    @Autowired
    TemplateService templateService;

    @Autowired
    ViseService viseService;

    @Autowired
    GlChuckService glChuckService;

    @Autowired
    YokeRingService yokeRingService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/formulario-ficha/{formType}")
    public String processSheetForm(
        @AuthenticationPrincipal UserDetails springUser,
        @RequestParam(value = "error", required = false) String errorMessage,
        @RequestParam(value = "submissionId", required = false) Integer submissionId,
        @PathVariable("formType") String formType,
        RedirectAttributes redirectAttributes,
        Model model){

        if (!FormType.isValidFormType(formType)) {
            redirectAttributes.addAttribute("error", "ERRO: Tipo de formulário inválido");
            return "redirect:/";
        }

        // These are the attributes every view this controller serves will need
        User user = userRepository.findByUsername(springUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Machine> machines = machineService.getAllMachines();
        List<Operation> operations = operationService.getAllOperations();
        List<Chuck> chucks = chuckService.getAllActiveChucks();
        List<Tool> tools = toolService.getAllActiveTools();

        model.addAttribute("user", user);
        model.addAttribute("machines", machines);
        model.addAttribute("operations", operations);
        model.addAttribute("chucks", chucks);
        model.addAttribute("tools", tools);

        if (errorMessage != null) {
            model.addAttribute("error", errorMessage);
        }

        // If the user provides a valid submissionId the form is 
        // prepopulated with the data from that submission.
        if (submissionId != null) {
            Optional<FormSubmission> optionalFormSubmission = formSubmissionService.getFormSubmissionById(submissionId);

            if (optionalFormSubmission.isPresent()) {
                FormSubmission formSubmission = optionalFormSubmission.get();
                List<FormSubmissionTool> toolList = formSubmissionToolService.getAllToolsBySubmissionId(submissionId);
                String cycleTime = DateAndTimeParser.formatCycleTime(formSubmission.getCycleTime());

                model.addAttribute("formSubmission", formSubmission);
                model.addAttribute("toolList", toolList);
                model.addAttribute("cycleTime", cycleTime);
            }else{
                model.addAttribute("error", "Não existe formulário com o ID: " + submissionId);
            }
        }

        switch (formType.toUpperCase()) {
            case "STANDARD":
                return "form-sheet";
        
            case "MILL":
                List<Template> templates = templateService.getAllActiveTemplates();
                List<Vise> vises = viseService.getAllActiveVises();

                model.addAttribute("templates", templates);
                model.addAttribute("vises", vises); 
                return "form-sheet-mill";

            case "DOUBLETURRET":
                return "form-sheet-doubleturret";

            case "AUTOMATIC":
                List<GlChuck> glChucks = glChuckService.getAllActiveGlChucks();
                List<YokeRing> yokeRings = yokeRingService.getAllActiveYokeRings();

                model.addAttribute("glChucks", glChucks);
                model.addAttribute("yokeRings", yokeRings);
                return "form-sheet-automatic";

            default:
                return "redirect:/";
        }
    }
    
    @PostMapping("/salvar-formulario/{formType}")
    public String saveFormSubmission(
        @PathVariable("formType") String formType,
        @ModelAttribute CompleteFormDataDTO completeFormDataDTO,
        RedirectAttributes redirectAttributes) {

        if (!FormType.isValidFormType(formType)) {
            redirectAttributes.addAttribute("error", "ERRO: Tipo de formulário inválido");
            return "redirect:/";
        }

        Integer submissionId;

        try {
            FormSubmission formSubmission = new FormSubmission();

            // Map data from CompleteFormDataDTO to FormSubmission
            formSubmission.setMachineId(completeFormDataDTO.getMachineId());
            formSubmission.setChuckId(completeFormDataDTO.getChuckId());
            formSubmission.setSecondChuckId(completeFormDataDTO.getSecondChuckId());
            formSubmission.setTemplateId(completeFormDataDTO.getTemplateId());
            formSubmission.setViseId(completeFormDataDTO.getViseId());
            formSubmission.setItem(completeFormDataDTO.getItem().toUpperCase());
            formSubmission.setItemDescription(completeFormDataDTO.getItemDescription().toUpperCase());
            formSubmission.setOperationId(completeFormDataDTO.getOperationId());
            formSubmission.setNcName(completeFormDataDTO.getNcName().toUpperCase());
            formSubmission.setCycleTime(DateAndTimeParser.parseCycleTimeToTotalSeconds(completeFormDataDTO.getCycleTime()));
            formSubmission.setUserId(completeFormDataDTO.getUserId());
            formSubmission.setChuckPressure(completeFormDataDTO.getChuckPressure());
            formSubmission.setHoldPressure(completeFormDataDTO.getHoldPressure());
            formSubmission.setCreatedAt(LocalDateTime.now());
            formSubmission.setIsSaved(false);
            formSubmission.setFormType(formType.toUpperCase());
            formSubmission.setGlChuckId(completeFormDataDTO.getGlChuckId());
            formSubmission.setYokeRingId(completeFormDataDTO.getYokeRingId());
            formSubmission.setGlSpeed(completeFormDataDTO.getGlSpeed());
            formSubmission.setRepetitionAmount(completeFormDataDTO.getRepetitionAmount());
            formSubmission.setGlNcName(completeFormDataDTO.getGlNcName());
            formSubmission.setPiecesPerFork(completeFormDataDTO.getPiecesPerFork());
            formSubmissionService.saveFormSubmission(formSubmission);

            submissionId = formSubmission.getSubmissionId(); 

            List<Integer> toolIds = Arrays.asList(
                completeFormDataDTO.getTool1Id(), completeFormDataDTO.getTool2Id(), completeFormDataDTO.getTool3Id(),
                completeFormDataDTO.getTool4Id(), completeFormDataDTO.getTool5Id(), completeFormDataDTO.getTool6Id(),
                completeFormDataDTO.getTool7Id(), completeFormDataDTO.getTool8Id(), completeFormDataDTO.getTool9Id(),
                completeFormDataDTO.getTool10Id());

            List<Integer> toolPositions = Arrays.asList(
                completeFormDataDTO.getTool1Position(), completeFormDataDTO.getTool2Position(), completeFormDataDTO.getTool3Position(),
                completeFormDataDTO.getTool4Position(), completeFormDataDTO.getTool5Position(), completeFormDataDTO.getTool6Position(),
                completeFormDataDTO.getTool7Position(), completeFormDataDTO.getTool8Position(), completeFormDataDTO.getTool9Position(),
                completeFormDataDTO.getTool10Position());

            List<Integer> toolLengths = Arrays.asList(
                completeFormDataDTO.getTool1Length(), completeFormDataDTO.getTool2Length(), completeFormDataDTO.getTool3Length(),
                completeFormDataDTO.getTool4Length(), completeFormDataDTO.getTool5Length(), completeFormDataDTO.getTool6Length(),
                completeFormDataDTO.getTool7Length(), completeFormDataDTO.getTool8Length(), completeFormDataDTO.getTool9Length(),
                completeFormDataDTO.getTool10Length());

            // Grouping tools in 2 groups: A (tools 1-5), B (tools 6-10).
            // This is used to determine where each tool will be shown
            // in the process-sheet-doubleturret.html, tools from group A will be
            // in the first toolSectionWrapper div and group B will be in the
            // second toolSectionWrapper div. 
            // The form layout (form-sheet-doubleturret.html) is set in a way
            // that is clear for the user that tools 1 - 5 are for the first
            // section and 6 - 10 will be shown at the second section.
            List<Character> toolGroups = Arrays.asList(
                'A', 'A', 'A', 'A', 'A', 'B', 'B', 'B', 'B', 'B');

            // Create list of FormSubmissionTool objects
            List<FormSubmissionTool> formSubmissionToolList = new ArrayList<>();
            for (int i = 0; i < toolIds.size(); i++) {
                Integer toolId = toolIds.get(i);
                if (toolId != null) {
                    formSubmissionToolList.add(new FormSubmissionTool(submissionId, toolId, toolLengths.get(i), toolPositions.get(i), toolGroups.get(i)));
                }
            }
            formSubmissionToolService.saveAllSubmissionToolsFromList(formSubmissionToolList);
        } catch (DataIntegrityViolationException e) {
            System.err.println(e.getMessage());
            redirectAttributes.addAttribute("error", "Erro de integridade de dados");
            return "redirect:/";
        } catch (Exception e) {
            System.err.println(e.getMessage());
            redirectAttributes.addAttribute("error", "Erro inesperado");
            return "redirect:/";
        }

        redirectAttributes.addAttribute("submissionId", submissionId);
        switch (formType.toUpperCase()) {
            case "STANDARD":
                return "redirect:/ficha/standard";

            case "MILL":
                return "redirect:/ficha/mill";

            case "DOUBLETURRET":
                return "redirect:/ficha/doubleturret";

            case "AUTOMATIC":
                return "redirect:/ficha/automatic";

            // This default case should never trigger but I kept it here
            // as a failsafe to the enum FormType validation method.
            default:
                redirectAttributes.addAttribute("error", "Erro ao direcionar ao formulario " + formType);
                return "redirect:/";
        }
    }

    @GetMapping("/ficha/{formType}")
    public String serveProcessSheet(
        @RequestParam("submissionId") Integer submissionId,
        @PathVariable("formType") String formType,
        @AuthenticationPrincipal UserDetails springUser,
        RedirectAttributes redirectAttributes,
        Model model) {

        if (!FormType.isValidFormType(formType)) {
            redirectAttributes.addAttribute("error", "ERRO: Tipo de formulário inválido");
            return "redirect:/";
        }

        if (submissionId != null) {
            User user = userRepository.findByUsername(springUser.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
            Optional<FormSubmission> optionalFormSubmission = formSubmissionService.getFormSubmissionById(submissionId); //fetch the form data from the DB

            if (optionalFormSubmission.isPresent()) {
                FormSubmission formSubmission = optionalFormSubmission.get();
                List<FormSubmissionToolDTO> formSubmissionTools = formSubmissionToolService.getAllToolsDTOBySubmissionId(submissionId);
                Machine machine = machineService.getMachineById(formSubmission.getMachineId()).orElse(null);
                String cycleTime = DateAndTimeParser.formatCycleTime(formSubmission.getCycleTime());
                Integer piecesPerHour = 3600 / formSubmission.getCycleTime(); //cycle time is in seconds in the db
                String dateString = DateAndTimeParser.parseTimesTampIntoCustomFormat(formSubmission.getCreatedAt()); //dd/mm/yy - hh:mm:ss
                
                if (formSubmission.getChuckId() != null) {
                    Chuck chuck = chuckService.getChuckById(formSubmission.getChuckId()).orElse(null);
                    model.addAttribute("chuck", chuck);
                };
                
                model.addAttribute("formSubmission", formSubmission);
                model.addAttribute("formSubmissionTools", formSubmissionTools);
                model.addAttribute("machine", machine);
                model.addAttribute("piecesPerHour", piecesPerHour);
                model.addAttribute("cycleTime", cycleTime);
                model.addAttribute("dateString", dateString);
                model.addAttribute("user", user);

                switch (formType.toUpperCase()) {
                    case "STANDARD":
                        return "process-sheet";

                    case "MILL":

                        if (formSubmission.getTemplateId() != null) {
                            Template template = templateService.getTemplateById(formSubmission.getTemplateId()).orElse(null);
                            model.addAttribute("template", template);
                        }
                        if (formSubmission.getViseId() != null) {
                            Vise vise = viseService.getViseById(formSubmission.getViseId()).orElse(null);
                            model.addAttribute("vise", vise);
                        }

                        return "process-sheet-mill";

                    case "DOUBLETURRET":
                        //need to separate the tools in two groups because the view has two sections for tools, cannot be arbitrary as the
                        //two different sections represent physical slots in the machines turret
                        List<FormSubmissionToolDTO> groupATools = formSubmissionTools.stream()
                                .filter(dto -> dto.getToolGroup() != null && dto.getToolGroup() == 'A')
                                .toList();
                
                        List<FormSubmissionToolDTO> groupBTools = formSubmissionTools.stream()
                                .filter(dto -> dto.getToolGroup() != null && dto.getToolGroup() == 'B')
                                .toList();

                        if (formSubmission.getSecondChuckId() != null) {
                            Chuck secondChuck = chuckService.getChuckById(formSubmission.getSecondChuckId()).orElse(null);
                            model.addAttribute("secondChuck", secondChuck);
                        };

                        model.addAttribute("groupATools", groupATools);
                        model.addAttribute("groupBTools", groupBTools);

                        return "process-sheet-doubleturret";

                    case "AUTOMATIC":
                        if (formSubmission.getGlChuckId() != null) {
                            GlChuck glChuck = glChuckService.getGlChuckById(formSubmission.getGlChuckId()).orElse(null);
                            model.addAttribute("glChuck", glChuck);
                        };

                        if (formSubmission.getYokeRingId() != null) {
                            YokeRing yokeRing = yokeRingService.getYokeRingById(formSubmission.getYokeRingId()).orElse(null);
                            model.addAttribute("yokeRing", yokeRing);
                        };
                        
                        return "process-sheet-automatic";

                    default:
                        redirectAttributes.addAttribute("error", "Erro ao direcionar ao formulario " + formType);
                        return "redirect:/";
                }
           }else{
                redirectAttributes.addAttribute("error", "Nenhum formulário com ID: " + submissionId);
                return "redirect:/";
           }
        }
        redirectAttributes.addAttribute("error", "Erro inesperado: entre em contato com o administrador");
        return "redirect:/";
    }

 @GetMapping("/ficha/documento")
    public ResponseEntity<String> serveSavedProcessSheet(@RequestParam("submissionId") Integer submissionId) {

        HtmlDocument htmlDocument = htmlDocumentService.getHtmlDocumentBySubmissionId(submissionId);

        if (htmlDocument == null) {
            return new ResponseEntity<>("Documento não encontrado: nenhum documento com ID: " + submissionId, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(htmlDocument.getHtmlData(), HttpStatus.OK);
    }

    @PostMapping("/salvar-ficha")
    public String saveProcessSheetToDatabase(
        @RequestParam("submissionId") Integer submissionId,
        @RequestParam("htmlData") String htmlData,
        RedirectAttributes redirectAttributes,
        Model model) {

        Optional<FormSubmission> optionalFormSubmission = formSubmissionService.getFormSubmissionById(submissionId);

        if (optionalFormSubmission.isPresent() && htmlData != null) {
            HtmlDocument existingHtmlDocument = htmlDocumentService.getHtmlDocumentBySubmissionId(submissionId);

            if (existingHtmlDocument != null) {
                existingHtmlDocument.setHtmlData(htmlData);
                htmlDocumentService.saveHtmlDocument(existingHtmlDocument);
            } else {
                HtmlDocument htmlDocument = new HtmlDocument();
                htmlDocument.setSubmissionId(submissionId);
                htmlDocument.setHtmlData(htmlData);
                htmlDocumentService.saveHtmlDocument(htmlDocument);
            }
            FormSubmission formSubmission = optionalFormSubmission.get();
            formSubmission.setIsSaved(true);
            formSubmissionService.saveFormSubmission(formSubmission);
            
        } else {
            redirectAttributes.addAttribute("error", "ERRO CRÍTICO: Parâmetros necessários não encontrados");
        }

        return "redirect:/";
    }

}
